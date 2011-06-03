#!/bin/sh

map=${HOME}/shared/map/otdt.map

echo "Checking diffs between trunk and versions specifed in ${map}"

for l in `cat $map`
do
  tag=`echo $l | sed -e "s/^.*,tag=\(tags\/[^\r]*\)[\r]*$/\1/"`
  if [ $tag != $l ]
  then
    path=`echo $l | sed -e "s/^.*,path=\(.*\),tag.*$/\1/"`
#    echo "${path} at ${tag}"
    url=`echo $l | sed -e "s/^.*,url=\(.*\),path.*$/\1/"`
    outfile=`echo ${path} | sed -e "s/plugins\/\(.*\)/\1/"`.diff
    svn diff ${url}/trunk/${path} ${url}/${tag}/${path} > $outfile 2> /dev/null
    if [ -s $outfile ]
    then
      echo $outfile
    else
      /bin/rm $outfile
    fi
  fi
done

echo "Done."

