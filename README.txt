///////////////////////////////////////
//          0.Release Notes          //
///////////////////////////////////////

----------Added features on v2.2.0-----------
- @ElevenZM's and @MuralAltDrive's mural arts can now be selected for thumbnail generation;
- Fixed compatibility issues with Start.gg (Smash.gg's new name);
- Fixed R.O.B icon color assignment for alts 1 and 2;
- Increased gson version to 2.8.9.



----------Added features on v2.1.1-----------
- Fixed issue when no image settings is found for a character, no message is shown to the user telling that;
- Fixed image settings names for Mii Gunner and Corrin;
- Increased log4j version to 2.17.1.



----------Added features on v2.1.0-----------
- Added Stream filter for Smash.gg thumbnail generation;
- Can assign different fighter image settings for each tournament;
- Added log support to track application usage for debug analysis;
- Fixed issue were Rosalina, Young Link and Incineroar would not show their preview icons;
- Fixed issue with fonts when no font styling (bold and italic) was selected, which would not allow thumbnail generation;
- Kazuya added to the roster;
- Sora added to the roster.



----------Added features on v2.0.0-----------
- Thumbnails can now be generated for Smash.gg tournaments;
- Tournament settings can be created, edited or deleted;
- Menu bar added to initial page to select Thumbnail Generator's functionalities.



----------Added features on v1.4.0-----------
- Scroll pane added to tournament list;
- Updated Falco horizontal offset from -10 to 60;
- Sephiroth added to the roster;
- Pyra/Mythra added to the roster.



----------Added features on v1.3.0-----------
- Steve added to the roster;
- Character icons appear when selecting a character and alt;
- A preview image of the chosen fighter and alt appears when clicking on character icon;
- Tournaments are now loaded from file "settings/tournaments/tournaments.json" to personalize available tournaments;
- Thumbnail settings such as font type and size can de adjusted on file "settings/thumbnail/text/text.json";
- Added "Pikmin Normal.tf" to allow creation of Alph Apparition thumbnails.



----------Added features on v1.2.2-----------
- Min Min added to the roster;
- Added Tiamat as a league.



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
	2.1. Thumbnails Save Directory
	2.2. Foregrounds, Backgrounds and Logos Directory
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

---------- 2.1. Thumbnails Save Directory -----------
A folder "thumbnails" is created during the generation of thumbnail.
This folder will be created on the folder where the executable is and it
will store all created thumbnails.


-------- 2.2. Foregrounds, Backgrounds and Logos Directory -------------
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
	-Tournament Diminutive (use for multi thumbnail generation);
	-Font;
	-Foreground;
	-Background;
	
As an alternative of creating a tournament from scratch, it is possible to create a duplicated 
tournament by selecting Edit -> Create copy of... -> <Tournament to copy> and change this 
copy's settings.
Field "Fighter Image Settings File" is not obligatory as the program will use as default the
following files for each art type:

    - RENDER: settings/thumbnails/images/default.json
    - MURAL: settings/thumbnails/images/defaultMural.json


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

As of version 2.2.0, the program is capable to generate thumbnails using characters art
from the "Everyone is here" mural/poster.
Character art isolation was done by @ElevenZM and mural arts for alts have been created by
@MuralAltDrive team which contains a GoogleDrive repository for these images.

    - Character isolations: https://www.deviantart.com/elevenzm/gallery/70115610/mural-isolations-super-smash-bros-ultimate
    - MuralAltDrive GoogleDrive: https://drive.google.com/drive/folders/1n4lAP6YB7N-bSSgEDx0OPEI2ykOKSQrM


----------- 3.2. Editing Fighters Images Settings ------------
The images needed some scaling and reposition in other to be printed 
correctly on the thumbnail. 

As mentioned in section 2.2, the program provides 2 default settings files,
one for each art type:

    - RENDER: settings/thumbnails/images/default.json
    - MURAL: settings/thumbnails/images/defaultMural.json

If users are not satisfied with current settings, they can create a copy of the
default .json files and change parameters for the specific character.
Afterwards, users must edit tournament settings so that the tournament is mapped
to the new fighter image settings file.

These settings files have the following structure:
    [{
    	"mirrorPlayer2": true,
    	"fighters":[
    	  {
    		"fighter": "mario",
    		"offset": [
    		  60,
    		  0
    		],
    		"scale": 0.7,
    		"flip": false
    	  },
    	  {
    		"fighter": "donkey_kong",
    		"offset": [
    		  30,
    		  30
    		],
    		"scale": 0.65,
    		"flip": true
    	  },...

 - mirrorPlayer2: if true, player 2 image will be flipped if flip = false;
 - fighter: character name as represented in Smash Ultimate website urls;
 - offset: horizontal (- left, + right) and vertical (- up, + down) offsets by pixels;
 - scale: scale multiplier for image resizing;
 - flip: tells whether image should be flipped (default = false).

NOTE: IF THE IMAGE IS FLIPPED (option), HORIZONTAL OFFSET ORIENTATION IS FLIPPED

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
As of version 2.2.0, first line can have a 3rd parameter to select character art (RENDER or MURAL)
If no additional parameter is provided, the program will use RENDER art per default.

- Each following line contains specific info for thumbnail generation (1 line = 1 thumbnail)

- 1st comes the players names. Then the characters. Afterwards, the alternate colors.
And finally, the name of the round played by the players
	Ex: Fizbo;Darkout;pikachu;pokemon_trainer;4;5;Winners Semis

- The characters names must be written the exact same way as shown in default.json and
defaultMural.json
 

----------- 4.2. Generating from a local file ------------
Thumbnails can be generated from a file that has a list of commands that
follows the rules in 4.1.

To load said file and start multi generation, users can click on 
"Generate from file" button or on File -> Generate thumbnails from file

Fighter images can be saved locally if "Save/Load fighter's image locally" checkbox
is checked before starting multi generation.


----------- 4.3. Generating from a Start.gg tournament ------------
Multi thumbnail generation of a Start.gg tournament is available on
File -> Generate thumbnails from Start.gg

During this generation, the following steps are performed by this Thumbnail Generator:

	1. Get list of events, phases and phase groups of tournament provided by user;
	2. Query Start.gg for sets on event, phase or phase group selected by user;
	3. Check which sets have a stream associated to them and write them on text area according to 4.1. rules;
	4. Generate thumbnails, according to text area commands, when clicking on "Generate thumbnails" button; 

It is important that users provide an Authorization Token before making any requests to Start.gg
To create your own Authorization Token, please look at the following:

	-https://developer.start.gg/docs/authentication/

When a Auth Token is given, users can provide tournament URL and, by clicking away from the corresponding
text box, users can query Start.gg to get available events, phases and phases groups.
After selecting an event, phase or phase group on the combo boxes, text commands can be generated by 
clicking on the arrow button that is in the middle of the window.
Depending on the number of participants of the tournament and the selections of the user, this step can
take some time.
As of now, Thumbnail Generator is unable to query Start.gg to provide only the sets that have a stream
associated to them.

Additional Notes:
	- Start.gg does not provide info on used alts and some tournaments may not even provide used characters (ex.: offline tournaments);
	- Fortunately, users can edit the commands directly on the text area;
	- Alts are set to the default alt and characters, when missing, are set to random;
	- Round names may also need to be edited in order to be less misleading;
	- If no commands are printed, it means that the program could not find, on provided list of sets, a set with a stream assigned.




///////////////////////////////////////
//             5. Others             //
///////////////////////////////////////

------------5.1. Character Alt Preview-------------------
When selecting a character from the dropdown, an icon will appear
This icon is the respective stock icon of the selected character.
Its color also changes when selecting a different alt.

To have a better look at the selected alt, the icon can be clicked
to show a fighter image that will appear on the thumbnail (character and alt).

The majority of the preview images are from the official Smash Bros. Ultimate site.
However, if the user has the respective fighter and alt saved on folder
"assets/fighters/", the program will open this images locally.

As of version 2.2.0, if user selects mural arts, the preview will redirect to the
mural arts stored in this program's repository or locally stored in "assets/mural".


------------5.2. Missing Mural Art-------------------
Version 2.2.0:
    Bowser Jr. - 3, 4, 5
    Ganondorf - 2, 4, 6
    Ike - 2, 4, 6, 8
    Incineroar - 5, 8
    Inkling - 4, 5, 6, 7, 8
    Jigglypuff - 3, 6, 7, 8
    Little Mac - 6, 8
    Mario - 8
    Marth - 3, 7
    Pichu - 2
    Pikachu - 4, 5, 6
    Sephiroth - 7, 8
    Shulk - 6, 7, 8
    Sonic - 7
    Sora - 2, 4, 6, 8
    Villager - 2, 3, 4, 6
    Random - 2 (Sandbag)



