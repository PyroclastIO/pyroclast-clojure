#!/bin/sh
set -e

echo "Deleting old build"
rm release-js/pyroclast.js
echo "Packing release-js/pyroclast.bare.js"
(cat release-js/wrapper.beg.txt; cat release-js/pyroclast.bare.js; cat release-js/wrapper.end.txt) > release-js/pyroclast.js
echo "Deleting intermediate artifacts"
rm release-js/pyroclast.bare.js
echo "Build finished, packed release-js/pyroclast.js"
