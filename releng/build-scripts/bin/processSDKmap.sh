#!/bin/bash

if [ $# != 1 ]
then
        echo "Usage: $0 SDKmapfile"
        exit 1
fi

infile=$1
if [ ! -r $infile ]
then
        echo "Can't read from $infile"
        exit 1
fi

echo "Snippet for otdt.map :"
echo "======================"

for p in org.eclipse.jdt.core.tests.builder \
                 org.eclipse.jdt.debug.tests \
                 org.eclipse.jdt.ui.tests \
                 org.eclipse.jdt.ui.tests.refactoring \
                 org.eclipse.jdt.text.tests \
                 org.eclipse.jface.text.tests \
                 org.eclipse.text.tests \
                 org.eclipse.core.filebuffers.tests
do
        grep $p= $infile
done
echo "======================"

echo "Snippet for build/test.properties :"
echo "==================================="

#define x.y.z versions (but replace . with _ since bash can't handle . in variable names):
org_eclipse_jdt_core_tests_builder=3.8.1
org_eclipse_test_performance=3.7.0
org_eclipse_jdt_debug_tests=3.8.0
org_eclipse_jdt_ui_tests=3.8.0
org_eclipse_jdt_ui_tests_refactoring=3.8.0
# currently no version qualifier:
#org_eclipse_jdt_core_tests_compiler
#org_eclipse_jdt_core_tests_model

for p in org.eclipse.jdt.core.tests.builder \
                org.eclipse.jdt.debug.tests \
                org.eclipse.jdt.ui.tests \
                org.eclipse.jdt.ui.tests.refactoring \
                org.eclipse.test.performance
do
        qual=`grep $p= $infile | sed -e "s|^.*=GIT,tag=\(.*\),repo=git.*$|\1|"`
        r=`echo $p | tr "." "_"`
        echo $p=${p}_${!r}.${qual}
done

echo "==================================="


