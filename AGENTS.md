# AGENTS.md

Read `README.md` first. Usage, best practices, number limits, boolean-as-`N` behaviour, code style there. Not repeated here.

Keep this file up to date. Change affects anything described here (stack, layout, codegen, commands, CI, test setup, conventions): update `AGENTS.md` in same change. Remove stale info, don't just append.

## Stack
- Java 17, Gradle, JUnit 6, Mockito, Lombok.
- Jackson 3: core/databind under `tools.jackson.*`; annotations stay `com.fasterxml.jackson.annotation.*`.
- AWS SDK v2 `dynamodb`. v1 SDK + v2 Enhanced Client test-only, for compatibility tests.
- `lombok.config`: fluent + chained accessors. Getter `obj.name()`, not `getName()`. Setters return `this`.

## Layout
- `buildSrc/` – codegen (JavaPoet). Reflects over `DynamoDbClient` / `DynamoDbAsyncClient` methods, writes `DynamoMapper.java` + `DynamoAsyncMapper.java`.
- `src/main/.../DynamoMapper.java`, `DynamoAsyncMapper.java` – **generated, never hand-edit**. Change generators in `buildSrc/`, run `./gradlew codegen`. SDK upgrades can change output too.
- Each SDK method gets: delegate wrapper (decoded `Mapped*Response`), `*FromPrimaryKey`, `*FromKeyObject`. Async adds `batchGetItemPaginator*`.
- `codec/DynamoEncoder` – POJO to Jackson tree to `AttributeValue`. Arrays always `L`, never `SS`/`NS`/`BS`.
- `codec/DynamoDecoder` – reverse. Integral `N` fitting `long` becomes long node; anything else string node, Jackson coerces to target type. Sets decode to arrays.
- `request/RequestFactory` – builds SDK requests from keys/key objects; `accept*Request` fills table name.
- `util/ReflectionUtil` – resolves `@DynamoTableName`, `@DynamoPrimaryKey`, applies `TableNameDecorator`.
- Builders, codec, request, util: handwritten.

## Commands
- After every change: `./gradlew spotlessApply compileIntegrationTestJava`. Formats + compiles all source sets. Does not run codegen.
- Unit tests: `./gradlew test`. Runs `codegen` first.
- Single class: `./gradlew test --tests 'com.autonomouslogic.dynamomapper.codec.DynamoDecoderTest'`.
- Integration: `./gradlew integrationTest`. Real AWS, see below. Don't run without credentials; ask.
- Never run `release.sh` / `publish.sh`. Never edit `CHANGELOG.md` (semantic-release writes it).

## CI (`.github/workflows`)
- `build.yml`: `test`, `integrationTest`, jacoco, codecov.
- `codegen.yml`: `codegen` + `spotlessApply` must leave zero diff. Commit regenerated mappers.
- `spotless.yml`: `spotlessCheck`.
- `semantic-pr.yml`: PR title must be conventional commit.

## Unit tests: `CodecTests`
- `src/test/.../test/CodecTests.java`: enum, one case per encoding. Each = `TestObject` POJO + expected `Map<String, AttributeValue>` + optional `EncodeDecode`.
- `EncodeDecode`: `null` both directions, `ENCODE_ONLY`, `DECODE_ONLY`.
- `DynamoEncoderTest` filters `encodeTest()`: `encoder.encode(pojo)` must equal map.
- `DynamoDecoderTest` filters `decodeTest()`: decodes map to `TestObject`, compares JSON of both (not `equals`). Also runs every case through each `map*Response`.
- `DECODE_ONLY` when encoder output differs from stored form: sets, `NUL`, `-0`, `N` as boolean. `// @todo` marks encoder gaps.
- New codec behaviour: add enum entry, add field to `TestObject` if needed. `TestObject` is `NON_NULL`; `boolPrimitive` is `NON_DEFAULT`.

## Integration tests (`src/integrationTest`)
- Real DynamoDB, no emulator. Table not created by tests. Needs `integration-test-table`, partition key `partitionKey` (S), TTL on `ttl`.
- Credentials: default AWS chain; `AWS_PROFILE` picks profile. CI uses `AWS_REGION`, `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`.
- Isolation: `IntegrationTestObjects.setKeyAndTtl` gives each item random `integration-<n>` key + 1 min TTL. No cleanup. Scan tests filter by random shared `str`.
- Data: `resources/integration-tests.jsonl`, one `IntegrationTestObject` per line, via `IntegrationTestUtil.loadIntegrationTestObjects()`. Feeds `PermutationTester` + `CompatibilityTest`.
- `DynamoMapperIntegrationTest` / `DynamoAsyncMapperIntegrationTest` mirror each other. Async `.join()`s; adds paginator tests via `PublisherUtil.collectBlocking`. New test in one: add to other.
- `PermutationTester`: every jsonl object × `MethodType` (`REQUEST`, `PRIMARY_KEY`, `KEY_OBJECT`) × `CallMethod` (`STRAIGHT`, `CONSUMER`). Lifecycle: get empty, put, get, update `str`, delete, get empty.
  - Test class injects mapper calls through setters (`getItemRequestStraight`, …). Same tester serves sync + async.
  - Put/update lack primary-key variant, fall back to key object.
  - Update/delete return item only for `REQUEST` or `CONSUMER` (they set `ReturnValue`). `CONSUMER` lambdas also assert prefilled table name/key.
- `IntegrationTestHelper`: shared cases. Number-as-boolean writes raw `N` via low-level client.
- `CompatibilityTest`: Dynamo Mapper vs official v1 `DynamoDBMapper` and v2 Enhanced Client, read + write both ways. Uses `CompatibilityTestObject` (annotated for all three; v2 needs bean getters/setters), converted from jsonl via JSON.
  - New `IntegrationTestObject` field used in jsonl: mirror in `CompatibilityTestObject` (`@DynamoDBAttribute` + getter/setter).
- `OfficialClientBooleanNumberProbeTest`: probes official clients reading boolean `N`. See its comments.

## Code style
- Code self-explanatory. Sensible function + variable names.
- Comments rare. Only when clarification necessary or code complicated. Keep short, never verbose.

## Git + PRs
- Commit message: single-line conventional commit, concise. Matches log: `fix: Decode a decimal N without throwing NumberFormatException`.
- Type drives release: `fix` patch, `feat` minor, `!` major.
- No commit footers. No `Co-Authored-By` or similar.
- Never force-push. Preserve branch history on PR.
- PR title: conventional commit. Body: problem solved + solution, concise, no fluff. Evidence where relevant. Assume reader knows existing code.
