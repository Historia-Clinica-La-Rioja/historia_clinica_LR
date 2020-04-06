#!/bin/sh
if  [ -z "$1" ]; then 
    export BRANCH="$(git branch | grep \* | cut -d ' ' -f2) ^master"
else
    export BRANCH="$1 ^origin/master"
fi
echo "Validando branch $BRANCH"

export BAD_COMMITS=$(git log $BRANCH --merges)
if  [ ! -z "$BAD_COMMITS" ]; then 
    echo "Error: Hay commits de merge. Este proyecto se trabaja con rebase, no debiera haber merge en los branches. Se muestran cuales:"
    echo "$BAD_COMMITS"
    exit 127
fi