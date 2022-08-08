#!/bin/bash

MAINDIR="assets/mural"
for file in $(find $MAINDIR -name '*.png')
do
  SUBDIR=$(echo $file| cut -d'/' -f 3)
  SUBSTRING1=$(echo $file| cut -d'(' -f 2)
  SUBSTRING2=$(echo $SUBSTRING1| cut -d')' -f 1)
  SUBSTRING3=$(echo $SUBSTRING2| cut -d'_' -f 2)
  if [ -z "SUBSTRING3" ]; then
	echo "Skipping $file"
  else
	NEWNAME="$MAINDIR/$SUBDIR/$SUBSTRING3.png"
	mv $file $NEWNAME
  fi 
done