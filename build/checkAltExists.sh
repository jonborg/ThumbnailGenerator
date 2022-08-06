#!/bin/bash

cd assets/mural
for d in * ; do
	for n in {1..8} ; do
		if [ -f "$d/$n.png" ] 
		then
			subrti=1
		else 
			echo "$d - $n"
		fi
	done
done
