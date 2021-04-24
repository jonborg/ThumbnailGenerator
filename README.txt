///////////////////////////////////////
//          0.Release Notes          //
///////////////////////////////////////

----------Added features on v2.0.0-----------
- Thumbnails can now be generated for Smash.gg tournaments
- Tournament settings can be created, edited or deleted.
- Menu bar added to initial page to select Thumbnail Generator's functionalities



----------Added features on v1.4.0-----------
- Scroll pane added to tournament list
- Updated Falco horizontal offset from -10 to 60
- Sephiroth added to the roster.
- Pyra/Mythra added to the roster.



----------Added features on v1.3.0-----------
- Steve added to the roster;
- Character icons appear when selecting a character and alt;
- A preview image of the chosen fighter and alt appears when clicking on character icon;
- Tournaments are now loaded from file "settings/tournaments/tournaments.json" to personalize available tournaments;
- Thumbnail settings such as font type and size can de adjusted on file "settings/thumbnail/text/text.json";
- Added "Pikmin Normal.tf" to allow creation of Alph Apparition thumbnails.



----------Added features on v1.2.2-----------
- Min Min added to the roster.
- Added Tiamat as a league



----------Added features on v1.2.1-----------
- Thumbnail font is now locally loaded. Now is not required to have the font installed on PC.



----------Added features on v1.2.0-----------
- Enabled text search when selecting fighters on their dropdowns;
- Errors during Thumbnail creation via file does not interrupt the parsing of the file anymore;
- Each cause of errors during Thumbnail creation via file is individually shown to the user;
- Some code improvements/cleanup was performed (let's see if it didn't break anything).



----------Added features on v1.1.0-----------
- Multi thumbnail generation from 1 file in csv syntax;
- Switch button that swaps player 1 and player 2 information;
- Added Smash or Pass online league thumbnail generation.




///////////////////////////////////////
//          1. Introduction          //
///////////////////////////////////////

-------------- 1.1. Index ------------------
0. Release Notes
1. Introduction
	1.1. Index
	1.2. Initial Configuration
2. Tournament Settings
	2.1. Thumbnails Save Directoy
	2.2. Foregrounds, Backgrounds and Logos Directoy
	2.3. Adding New Tournaments
	2.4. Deleting Tournaments
3. Fighter Image Settings 
	3.1. Fighter Images 
	3.2. Editing Fighters Images Settings
4. Multi Thumbnail Generation
	4.1. Basic rules
	4.2. Generating from a local file
	4.3. Generating from a Smash.gg tournament
5. Others
	5.1. Character Alt Preview
	

------------- 1.2. Initial Configuration ----------------
Click on ThumbnailGenerator.exe to start the program. 

Folder "assets" and folder "settings" need to be in the same directory
of the executable in order to work properly.

If there are any personalized information on previous versions,
you only need to move the replace the old executable with the new
one.




///////////////////////////////////////
//      2. Tournament Settings       //
///////////////////////////////////////

---------- 2.1. Thumbnails Save Directoy -----------
A folder "thumbnails" is created during the generation of thumbnail.
This folder will be created on the folder where the executable is and it
will store all created thumbnails.


-------- 2.2. Foregrounds, Backgrounds and Logos Directoy -------------
As default, foreground and background images of thumbnails as well as tournament logos
are stored on the following folder:

	-Default location: "assets/tournaments"

Users can use images of other folders by changing parameters on "tournaments.json"

When adding new foregrounds, backgrounds and logos, make sure they have the following dimensions:
	
	-foregrounds: 1280x720;
	-backgrounds: 1280x720;
	-logos: 200x200.
	
	
---------- 2.3. Adding Tournaments ----------------
Tournament settings are save on the following file:

	-Tournaments file: settings/tournaments/tournaments.json

To add a new tournament, go to Edit -> Create new tournament.
A new window will appear where users can configure how thumbnails will be created 
for the new tournament.
The following settings are obligatory to be provided in order to successfully create the tournament.
	
	-Tournament Name;
	-Tournament Diminutive (use for multi thumnail generation);
	-Font;
	-Foreground;
	-Background;
	
As an alternative of creating a tournament from scratch, it is possible to create a duplicated 
tournament by selecting Edit -> Create copy of... -> <Tournament to copy> and change this 
copy's settings.


---------- 2.4. Deleting Tournaments ----------------
tournament settings can be deleted in Edit -> Delete tournament -> <Tournament to delete>.
A dialog box will appear to confirm whether this deletion is intentional.




///////////////////////////////////////
//     3. Fighter Image Settings     //
///////////////////////////////////////

-------------- 3.1. Fighter Images ----------------
The majority of the fighter images are downloaded directly from
the official page of Super Smash Bros. Ultimate website.

As an option, Thumbnail Generator can save those downloaded images
to use them for future uses without requiring access to the website
This option can be used during multi thumbnail generation

	-Fighter images path: "assets/fighters/"


----------- 3.2. Editing Fighters Images Settings ------------
The images needed some scaling and reposition in other to be printed 
correctly on the thumbnail. 

Each fighters scaling, reposition and mirroring settings are found on these files:

 - scaling: "settings/thumbnails/images/scale.txt"
 - offset: "settings/thumbnails/images/offset.txt"
 - flip: "settings/thumbnails/images/flip.txt"


--SCALE.TXT
Each line of scale.txt has a fighter name and its image multiplier for
image resizing: 
Ex.: mario 0.7

--OFFSET.TXT
Each line of offset.txt has a fighter name and 2 numbers representing
horizontal and vertical translation: 
Ex.: mario 60 0


The first number shows how many pixels the image moves to the right.
NOTE: IF THE IMAGE IS FLIPPED (option), THE IMAGE WILL MOVE TO THE 
LEFT INSTEAD

The second number shows how many pixels the image moves downwards.

--FLIP.TXT
Each line of flip.txt has a fighter name and a boolena value telling
if image should be mirrored when fighter is on player 1 side: 
Ex.: mario false


All these values can be changed to fit users preferences and the updated values 
will be used on the next thumbnail generation.




///////////////////////////////////////
//   4. Multi Thumbnail Generation   //
///////////////////////////////////////

----------- 4.1. Basic rules ------------
Multiple thumbnails can ge generated at once thanks to a certain order of commands.
These commands must follow these rules:

- All parameters must be separated by a ";"

- First line must have tournament id and event name/date (ex.:sop;05/12/2020)

- Each following line contains specific info for thumbnail generation (1 line = 1 thumbnail)

- 1st comes the players names. Then the characters. Afterwards, the alternate colors.
And finally, the name of the round played by the players
	Ex: Fizbo;Darkout;pikachu;pokemon_trainer;4;5;Winners Semis

- The characters names must be written the exact same way as shown in scale.txt, 
offset.txt and flip.txt
 

----------- 4.2. Generating from a local file ------------
Thumbnails can be generated from a file that has a list of commands that
follows the rules in 4.1.

To load said file and start multi generation, users can click on 
"Generate from file" button or on File -> Generate thumbnails from file

Fighter images can be saved locally if "Save/Load fighter's image locally" checkbox
is checked before starting multi generation.


----------- 4.3. Generating from a Smash.gg tournament ------------
Multi thumbnail generation of a Smash.gg tournament is available on
File -> Generate thumbnails from Smash.gg

During this generation, the following steps are performed by this Thumbnail Generator:

	1. Get list of events, phases and phase groups of tournament provided by user;
	2. Query Smash.gg for sets on event, phase or phase group selected by user;
	3. Check which sets have a stream associated to them and write them on text area according to 4.1. rules;
	4. Generate thumbnails, according to text area commands, when clicking on "Generate thumbnails" button; 

It is important that users provide an Authorization Token before making any requests to Smash.gg
To create your own Authorization Token, please look at the following:

	-https://developer.smash.gg/docs/authentication/

When a Auth Token is given, users can provide tournament URL and, by clicking away from the corresponding
text box, users can query Smash.gg to get available events, phases and phases groups.
After selecting an event, phase or phase group on the combo boxes, text commands can be generated by 
clicking on the arrow button that is in the middle of the window.
Depending on the number of participants of the tournament and the selections of the user, this step can
take some time.
As of now, Thumbnail Generator is unable to query Smash.gg to provide only the sets that have a stream
associated to them.

Additional Notes:
	- Smash.gg does not provide info on used alts and some tournaments may not even provide used characters (ex.: offline tournaments);
	- Fortunately, users can edit the commands directly on the text area;
	- Alts are set to the default alt and characters, when missing, are set to CHAR1 or CHAR2;
	- Round names may also need to be edited in order to be less misleading;
	- If no commands are printed, it means that the program could not find, on provided list of sets, a set with a stream assigned.




///////////////////////////////////////
//             5. Others             //
///////////////////////////////////////

------------5.1. Character Alt Preview-------------------
When selecting a character from the dropdown, an icon will appear
This icon is the repective stock icon of the selected character.
Its color also changes when selecting a different alt.

To have a better look at the selected alt, the icon can be clicked
to show a fighter image that will appear on the thumbnail (character and alt).

The majority of the preview images are from the official Smash Bros. Ultimate site.
However, if the user has the respective fighter and alt saved on folder
"assets/fighters/", the program will open this images locally.



