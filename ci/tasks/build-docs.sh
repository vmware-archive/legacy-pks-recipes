#!/bin/bash

set -e

pushd recipes-repo
    mkdocs build
    cp -R site/* ../recipes-docs/
popd

pushd recipes-docs
AUTH_KEYS=$(cat <<EOF
$AUTH_USER:$AUTH_PASS
EOF
)

echo "$AUTH_KEYS" > Staticfile.auth

MANIFEST=$(cat <<EOF
---
applications:
- name: app0cookbook
  memory: 64M
  buildpack: staticfile_buildpack
EOF
)

echo "$MANIFEST" > manifest.yml
popd