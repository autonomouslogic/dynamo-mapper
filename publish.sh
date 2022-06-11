#!/bin/bash -ex

# Workaround for annotated tags.
# https://github.com/semantic-release/semantic-release/pull/1871#issuecomment-980015434
# https://github.com/autonomouslogic/semantic-release-gradle-sonatype-example/issues/5
VERSION="$1"
git tag -a -f -m $VERSION $VERSION
if ./gradlew properties | grep version | grep -q -; then
  echo Cannot publish development version
  ./gradlew properties | grep version
  git tag -d $VERSION
  git push origin :refs/tags/$VERSION
  exit 1
fi
git push --force origin $VERSION

./gradlew clean

./gradlew publishToSonatype closeSonatypeStagingRepository --stacktrace
# Use the command below to automatically release.
#./gradlew publishToSonatype closeAndReleaseSonatypeStagingRepository --stacktrace
