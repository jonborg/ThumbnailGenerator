This is an instructions manual for v1.1.0. of Thumbnail Generator

----------Added features for v1.1.0-----------
- Multi thumbnail generation from 1 file in csv syntax;
- Switch button that swaps player 1 and player 2 information;
- Added Smash or Pass online league thumbnail generation;




-----------------Introduction-----------------
Click on ThumbnailGenerator.exe to start the program. 

It needs to be on the same folder as the folder "resources" in order
to function properly.




-----------Thumbnails save location-----------
Thumnails are saved in the folder "thumbnails", that is created when
generating your first thumbnail.





--------------Compatible Leagues--------------
ThumbnailGenerator can create thumbnails for the following leagues:
	-Throwdown Lx
	-Smash Invicta
	-Smash or Pass



---------Support images save location---------
Support images are found in the following paths. 

If you want to use another image (ex.:background), save the new 
image on the target folder and rename it like the old image 
(ex.:background.png)

 -Background image: resources/images/others/background.png 
 -Foreground image (Throwndown Lx): resources/images/others/foregroundLx.png
 -Foreground image (Smash Invicta): resources/images/others/foregroundPorto.png





----------------Fighter Images----------------
The majority of the fighter images are downloaded directly from
the official page of Super Smash Bros. Ultimate website.

As an option, Thumbnail Generator can save those downloaded images
to use them for future uses without requiring access to the website

 -Fighter images path: resources/fighters/




-----------Editing fighters images------------
The images needed some scaling and reposition in other to be printed 
correctly on the thumbnail. 

Each fighters scalling and reposition is found on these files:

 - scalling: resources/config/scale.txt
 - offset: resources/config/offset.txt


Each line of scale.txt has a fighter name and its image multiplier for
image resizing: 
Ex.: mario 0.7


Each line of offset.txt has a fighter name and 2 numbers representing
horizontal and vertical translation: 
Ex.: mario 60 0

The first number shows how many pixels the image moves to the right.
NOTE: IF THE IMAGE IS FLIPPED (option), THE IMAGE WILL MOVE TO THE 
LEFT INSTEAD

The second number shows how many pixels the image moves downwards.

These values can be changed to fit users preferences and the updated values 
will be used on the next thumbnail generation.




-----------Multi thumbnail generation------------
Rules:
- (All parameters must be separated by a ";"
- First line must have league + date
 	Throwdown Lx - tdlx
	Invicta - invicta
	Smash or Pass - sop

- Each following line contains info for thumbnail generation (1 line = 1 thumbnail)
- 1st comes the players names. Then the characters. Afterwards, the alternate colors.
And finally, the name of the round played by the players
	Ex: fizbo;Darkout;pikachu;pokemon_trainer;4;5;winners Semis

- The characters names must be written the exact same way as shown in scale.txt
and offset.txt
	








ation
 


