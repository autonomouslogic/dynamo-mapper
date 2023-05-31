Dynamo Mapper Changelog

## [2.1.0](https://github.com/autonomouslogic/dynamo-mapper/compare/2.0.5...2.1.0) (2023-05-31)


### Features

* Table name decorator ([#100](https://github.com/autonomouslogic/dynamo-mapper/issues/100)) ([5d99a3a](https://github.com/autonomouslogic/dynamo-mapper/commit/5d99a3a7edb7eef391ed49091b8d8ade36778f9f))


### Miscellaneous Chores

* **deps:** update plugin org.danilopianini.git-sensitive-semantic-versioning-gradle-plugin to v1 ([#92](https://github.com/autonomouslogic/dynamo-mapper/issues/92)) ([2997b3c](https://github.com/autonomouslogic/dynamo-mapper/commit/2997b3cb2b44c3c50803ae57c896f4ae178e9e3e))

## [2.0.5](https://github.com/autonomouslogic/dynamo-mapper/compare/2.0.4...2.0.5) (2023-05-01)


### Bug Fixes

* **deps:** update all non-major dependencies ([#99](https://github.com/autonomouslogic/dynamo-mapper/issues/99)) ([204ddd7](https://github.com/autonomouslogic/dynamo-mapper/commit/204ddd7d7461423a38df449a7f3affadd69477de))

## [2.0.4](https://github.com/autonomouslogic/dynamo-mapper/compare/2.0.3...2.0.4) (2023-04-03)


### Bug Fixes

* **deps:** update all non-major dependencies ([#98](https://github.com/autonomouslogic/dynamo-mapper/issues/98)) ([fd5c70a](https://github.com/autonomouslogic/dynamo-mapper/commit/fd5c70a8f7bc30018ccf94f9861d1afe4f4b52b2))

## [2.0.3](https://github.com/autonomouslogic/dynamo-mapper/compare/2.0.2...2.0.3) (2023-03-23)


### Bug Fixes

* **deps:** update dependency org.mockito:mockito-core to v5 ([#93](https://github.com/autonomouslogic/dynamo-mapper/issues/93)) ([ca3751e](https://github.com/autonomouslogic/dynamo-mapper/commit/ca3751edcfa403b66571b8ebe1579f6265de5c77))


### Miscellaneous Chores

* **deps:** update all non-major dependencies ([#96](https://github.com/autonomouslogic/dynamo-mapper/issues/96)) ([9a8c2a4](https://github.com/autonomouslogic/dynamo-mapper/commit/9a8c2a4faa6c33956f5ff93b4259e4736dde02d0))
* **deps:** update plugin io.freefair.lombok to v8 ([#97](https://github.com/autonomouslogic/dynamo-mapper/issues/97)) ([1eba09d](https://github.com/autonomouslogic/dynamo-mapper/commit/1eba09deb5913285d4abad1ec79172ddc51e3450))

## [2.0.2](https://github.com/autonomouslogic/dynamo-mapper/compare/2.0.1...2.0.2) (2023-02-06)


### Bug Fixes

* **deps:** update all non-major dependencies ([#94](https://github.com/autonomouslogic/dynamo-mapper/issues/94)) ([844f2b1](https://github.com/autonomouslogic/dynamo-mapper/commit/844f2b1ad71ad074a6f0b21b80335ad9f17b2024))

## [2.0.1](https://github.com/autonomouslogic/dynamo-mapper/compare/2.0.0...2.0.1) (2023-01-02)


### Bug Fixes

* **deps:** update all non-major dependencies ([#91](https://github.com/autonomouslogic/dynamo-mapper/issues/91)) ([92b6af4](https://github.com/autonomouslogic/dynamo-mapper/commit/92b6af433ba60025a7381abab3fade8abcd05c9f))
* Fixed generic return on async primary key object ([#90](https://github.com/autonomouslogic/dynamo-mapper/issues/90)) ([ce13070](https://github.com/autonomouslogic/dynamo-mapper/commit/ce1307039c6bf4afdda977bf3a11b646c8d03859))


### Miscellaneous Chores

* Added Lombok generated annotations ([#86](https://github.com/autonomouslogic/dynamo-mapper/issues/86)) ([725822f](https://github.com/autonomouslogic/dynamo-mapper/commit/725822f44b6b94b75450650e29b6cdc03edaefe0))
* **deps:** update all non-major dependencies ([#82](https://github.com/autonomouslogic/dynamo-mapper/issues/82)) ([819b61c](https://github.com/autonomouslogic/dynamo-mapper/commit/819b61c6720a498e018ee71e822c55e0d669a1ac))


### Code Refactoring

* Code generation refactor ([#87](https://github.com/autonomouslogic/dynamo-mapper/issues/87)) ([2e8766f](https://github.com/autonomouslogic/dynamo-mapper/commit/2e8766f8b3b661956ce9a0bcbbceb000cadbad5c))
* Codegen refactor ([#80](https://github.com/autonomouslogic/dynamo-mapper/issues/80)) ([630777d](https://github.com/autonomouslogic/dynamo-mapper/commit/630777d8fad4b89cf1de10498dfead9f5c6f1d0e))


### Tests

* Fixed v2 compatibility testing ([#89](https://github.com/autonomouslogic/dynamo-mapper/issues/89)) ([8443499](https://github.com/autonomouslogic/dynamo-mapper/commit/8443499f3ff890beb9e54442d91f2396094dfd2d))
* More thorough integration testing ([#88](https://github.com/autonomouslogic/dynamo-mapper/issues/88)) ([5866ae9](https://github.com/autonomouslogic/dynamo-mapper/commit/5866ae9bf56cb4a4a9d211da788ca40dad6d4d78))
* More thorough integration testing ([#88](https://github.com/autonomouslogic/dynamo-mapper/issues/88)) ([f0e2fb1](https://github.com/autonomouslogic/dynamo-mapper/commit/f0e2fb13edf286800ea54bb636c1e0fc80bcae21))

## [2.0.0](https://github.com/autonomouslogic/dynamo-mapper/compare/1.2.23...2.0.0) (2022-12-04)


### ⚠ BREAKING CHANGES

* Renamed primary key and key object methods. (#75)
* Renamed @DynamoHashKey to @DynamoPrimaryKey. (#77)

### Features

* Batch gets ([#74](https://github.com/autonomouslogic/dynamo-mapper/issues/74)) ([7451d11](https://github.com/autonomouslogic/dynamo-mapper/commit/7451d113603f4116e81744dea62f46e826520686))
* Renamed @DynamoHashKey to @DynamoPrimaryKey. ([#77](https://github.com/autonomouslogic/dynamo-mapper/issues/77)) ([b6208c3](https://github.com/autonomouslogic/dynamo-mapper/commit/b6208c35763955584ad7394c7c68a969b8777656))
* Renamed primary key and key object methods. ([#75](https://github.com/autonomouslogic/dynamo-mapper/issues/75)) ([23880c0](https://github.com/autonomouslogic/dynamo-mapper/commit/23880c0b915bbe16574704d0296ab4e66cb78bb0))


### Bug Fixes

* Fixed batch get items from key objects. Added more testing. ([#79](https://github.com/autonomouslogic/dynamo-mapper/issues/79)) ([5ea7468](https://github.com/autonomouslogic/dynamo-mapper/commit/5ea746849e976dc22ccffada1bb358a00210f842))


### Documentation

* Readme. ([608bfb4](https://github.com/autonomouslogic/dynamo-mapper/commit/608bfb4e40b047cd725b65759708f2031ac21960))


### Continuous Integration

* Separated out codegen check ([#78](https://github.com/autonomouslogic/dynamo-mapper/issues/78)) ([3cb38a0](https://github.com/autonomouslogic/dynamo-mapper/commit/3cb38a0e8817451bd8ad4ddd1bf681bf7ca189df))

## [1.2.23](https://github.com/autonomouslogic/dynamo-mapper/compare/1.2.22...1.2.23) (2022-11-07)


### Bug Fixes

* **deps:** update all non-major dependencies ([#73](https://github.com/autonomouslogic/dynamo-mapper/issues/73)) ([ab193cc](https://github.com/autonomouslogic/dynamo-mapper/commit/ab193cca454e0870d4ec300fd5053b757eb77458))


### Miscellaneous Chores

* **deps:** update amannn/action-semantic-pull-request action to v5 ([#72](https://github.com/autonomouslogic/dynamo-mapper/issues/72)) ([57bd541](https://github.com/autonomouslogic/dynamo-mapper/commit/57bd541ec885b3e384c7553dcebdebffa1ba5ffa))

## [1.2.22](https://github.com/autonomouslogic/dynamo-mapper/compare/1.2.21...1.2.22) (2022-10-03)


### Bug Fixes

* **deps:** update all non-major dependencies ([#71](https://github.com/autonomouslogic/dynamo-mapper/issues/71)) ([acd597a](https://github.com/autonomouslogic/dynamo-mapper/commit/acd597af32bde2f2208fe13e3124e18e31145a89))


### Styles

* Spotless formatting ([#69](https://github.com/autonomouslogic/dynamo-mapper/issues/69)) ([074c86f](https://github.com/autonomouslogic/dynamo-mapper/commit/074c86f68683aca41959105aa55f915995424545))


### Continuous Integration

* Abort build of code changes ([#70](https://github.com/autonomouslogic/dynamo-mapper/issues/70)) ([f521a79](https://github.com/autonomouslogic/dynamo-mapper/commit/f521a791c630f9622c3a18a86b21747833993942))

## [1.2.21](https://github.com/autonomouslogic/dynamo-mapper/compare/1.2.20...1.2.21) (2022-09-23)


### Bug Fixes

* **deps:** update dependency software.amazon.awssdk:dynamodb to v2.17.279 ([#67](https://github.com/autonomouslogic/dynamo-mapper/issues/67)) ([6f680c2](https://github.com/autonomouslogic/dynamo-mapper/commit/6f680c23a4d23646a845040f76b169a1c7f9493f))


### Continuous Integration

* Removed custom package rules again. ([06ae913](https://github.com/autonomouslogic/dynamo-mapper/commit/06ae9138c5023fc90308975f933bcb0d50dd33cf))

## [1.2.20](https://github.com/autonomouslogic/dynamo-mapper/compare/1.2.19...1.2.20) (2022-09-21)


### Bug Fixes

* **deps:** update all non-major dependencies ([#66](https://github.com/autonomouslogic/dynamo-mapper/issues/66)) ([d72f880](https://github.com/autonomouslogic/dynamo-mapper/commit/d72f880d630d32d86eecec8b979dabfd2d3f30b0))

## [1.2.19](https://github.com/autonomouslogic/dynamo-mapper/compare/1.2.18...1.2.19) (2022-09-14)


### Bug Fixes

* **deps:** update all non-major dependencies ([#64](https://github.com/autonomouslogic/dynamo-mapper/issues/64)) ([d3963d6](https://github.com/autonomouslogic/dynamo-mapper/commit/d3963d6708ddce930d6e03007d25e7332767a03d))

## [1.2.18](https://github.com/autonomouslogic/dynamo-mapper/compare/1.2.17...1.2.18) (2022-09-13)


### Bug Fixes

* **deps:** update dependency software.amazon.awssdk:dynamodb to v2.17.272 ([#65](https://github.com/autonomouslogic/dynamo-mapper/issues/65)) ([a4b1c49](https://github.com/autonomouslogic/dynamo-mapper/commit/a4b1c49b0eef97816ec6fbc1aa43a33c3c4ad560))

## [1.2.17](https://github.com/autonomouslogic/dynamo-mapper/compare/1.2.16...1.2.17) (2022-09-07)


### Bug Fixes

* **deps:** update dependency software.amazon.awssdk:dynamodb to v2.17.267 ([#63](https://github.com/autonomouslogic/dynamo-mapper/issues/63)) ([d2f109b](https://github.com/autonomouslogic/dynamo-mapper/commit/d2f109b24ef46643321922b3402faea55e46a344))

## [1.2.16](https://github.com/autonomouslogic/dynamo-mapper/compare/1.2.15...1.2.16) (2022-09-06)


### Bug Fixes

* **deps:** update all non-major dependencies ([#62](https://github.com/autonomouslogic/dynamo-mapper/issues/62)) ([c6a5b66](https://github.com/autonomouslogic/dynamo-mapper/commit/c6a5b669a0004838c0f43fdf688333ff7be76617))


### Continuous Integration

* Changed DynamoDB update to fix scope. ([b1e851f](https://github.com/autonomouslogic/dynamo-mapper/commit/b1e851f3280ae97460dcc3a20a2c9a2bad5face8))

## [1.2.15](https://github.com/autonomouslogic/dynamo-mapper/compare/1.2.14...1.2.15) (2022-09-01)


### Bug Fixes

* **deps:** update all non-major dependencies ([#60](https://github.com/autonomouslogic/dynamo-mapper/issues/60)) ([4a11a77](https://github.com/autonomouslogic/dynamo-mapper/commit/4a11a7737e764ed1cae3ff091fdc65c4abc8379f))

## [1.2.14](https://github.com/autonomouslogic/dynamo-mapper/compare/1.2.13...1.2.14) (2022-08-31)


### Bug Fixes

* **deps:** update all non-major dependencies ([#59](https://github.com/autonomouslogic/dynamo-mapper/issues/59)) ([52ff931](https://github.com/autonomouslogic/dynamo-mapper/commit/52ff931d71a9263293c1a9537695a027e0d07453))


### Continuous Integration

* Another attempt at separated dependencies ([b0b90e8](https://github.com/autonomouslogic/dynamo-mapper/commit/b0b90e847a08f260a0c4ebe1bd33ee469869efcb))
* disabled auto-releases on dependency updates ([8277fb0](https://github.com/autonomouslogic/dynamo-mapper/commit/8277fb017b520c2fa1054212ffb996b1bdf0353e))
* Switched AWS match to pattern. [skip ci] ([a33d6bf](https://github.com/autonomouslogic/dynamo-mapper/commit/a33d6bf3675b145eb6ac52021e42a8b1b9280e47))
* v1 AWS SDK as test dependency [skip ci] ([73d0ff7](https://github.com/autonomouslogic/dynamo-mapper/commit/73d0ff7500683f4db0c528890e05f227665eea52))

## [1.2.13](https://github.com/autonomouslogic/dynamo-mapper/compare/1.2.12...1.2.13) (2022-08-23)


### Bug Fixes

* **deps:** update all non-major dependencies ([#58](https://github.com/autonomouslogic/dynamo-mapper/issues/58)) ([5584641](https://github.com/autonomouslogic/dynamo-mapper/commit/55846417f94062d6900bd6498922d9451b58fb18))


### Continuous Integration

* Switched to shared Renovte config [skip ci] ([f07588b](https://github.com/autonomouslogic/dynamo-mapper/commit/f07588b9222c902a199ff0d5d36b6ad43f5dbe49))


### Build System

* changed shared config repo name. ([8cd6a2b](https://github.com/autonomouslogic/dynamo-mapper/commit/8cd6a2b4484965cefb282839a16c054abb4e69df))

## [1.2.12](https://github.com/autonomouslogic/dynamo-mapper/compare/1.2.11...1.2.12) (2022-08-10)


### Miscellaneous Chores

* **deps:** update all non-major dependencies to v7.5.1 ([#56](https://github.com/autonomouslogic/dynamo-mapper/issues/56)) ([cb44465](https://github.com/autonomouslogic/dynamo-mapper/commit/cb444659eb888c64086f1199eff2512eba98d226))

## [1.2.11](https://github.com/autonomouslogic/dynamo-mapper/compare/1.2.10...1.2.11) (2022-08-02)


### Bug Fixes

* **deps:** update all non-major dependencies ([#55](https://github.com/autonomouslogic/dynamo-mapper/issues/55)) ([0982748](https://github.com/autonomouslogic/dynamo-mapper/commit/098274811a5ff7dd04ca169380900e17fb2bdda9))

## [1.2.10](https://github.com/autonomouslogic/dynamo-mapper/compare/1.2.9...1.2.10) (2022-07-29)


### Bug Fixes

* **deps:** update all non-major dependencies ([#54](https://github.com/autonomouslogic/dynamo-mapper/issues/54)) ([62f6b04](https://github.com/autonomouslogic/dynamo-mapper/commit/62f6b0444bb75676c785ffe81eac451850406d20))

## [1.2.9](https://github.com/autonomouslogic/dynamo-mapper/compare/1.2.8...1.2.9) (2022-07-20)


### Miscellaneous Chores

* **deps:** update all non-major dependencies ([#53](https://github.com/autonomouslogic/dynamo-mapper/issues/53)) ([d53b896](https://github.com/autonomouslogic/dynamo-mapper/commit/d53b896a003cfbe9fe4ab197792b4cfc6b40f099))

## [1.2.8](https://github.com/autonomouslogic/dynamo-mapper/compare/1.2.7...1.2.8) (2022-07-14)


### Miscellaneous Chores

* **deps:** update all non-major dependencies ([#52](https://github.com/autonomouslogic/dynamo-mapper/issues/52)) ([1a651d5](https://github.com/autonomouslogic/dynamo-mapper/commit/1a651d552a10840e19f1842c171d9fa1097eca75))

## [1.2.7](https://github.com/autonomouslogic/dynamo-mapper/compare/1.2.6...1.2.7) (2022-07-05)


### Miscellaneous Chores

* **deps:** update all non-major dependencies ([#51](https://github.com/autonomouslogic/dynamo-mapper/issues/51)) ([d8379b5](https://github.com/autonomouslogic/dynamo-mapper/commit/d8379b5ac74b714cd10ed133a687af483344dd76))

## [1.2.6](https://github.com/autonomouslogic/dynamo-mapper/compare/1.2.5...1.2.6) (2022-06-28)


### Miscellaneous Chores

* **deps:** update all non-major dependencies ([#50](https://github.com/autonomouslogic/dynamo-mapper/issues/50)) ([9ec63f5](https://github.com/autonomouslogic/dynamo-mapper/commit/9ec63f5555b92094d8df1e2affb12ba4770cb44e))

## [1.2.5](https://github.com/autonomouslogic/dynamo-mapper/compare/1.2.4...1.2.5) (2022-06-22)


### Continuous Integration

* fixed .releaserc with correct format, added a changelog, and added more sections for release notes. ([feaf7ed](https://github.com/autonomouslogic/dynamo-mapper/commit/feaf7ed67d7270fb7bf0169342083084fb12d5e3))
* Limit minor dependency updates to Mondays. ([f564c52](https://github.com/autonomouslogic/dynamo-mapper/commit/f564c521cfa92a9d87c673862d651a81698c5108))


### Miscellaneous Chores

* **deps:** update all non-major dependencies ([#49](https://github.com/autonomouslogic/dynamo-mapper/issues/49)) ([00d2db8](https://github.com/autonomouslogic/dynamo-mapper/commit/00d2db815193ff5315204c9629c9eb48739601d3))
