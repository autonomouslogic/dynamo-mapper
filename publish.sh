#!/bin/bash -ex

# Workaround for annotated tags.
# https://github.com/semantic-release/semantic-release/pull/1871#issuecomment-980015434
# https://github.com/autonomouslogic/semantic-release-gradle-sonatype-example/issues/5
VERSION="$1"
git tag -a -f -m $VERSION $VERSION
git push --force origin $VERSION

./gradlew clean

#./gradlew publishToSonatype closeSonatypeStagingRepository
# Use the command below to automatically release.
./gradlew publishToSonatype closeAndReleaseSonatypeStagingRepository
