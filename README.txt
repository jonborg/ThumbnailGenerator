///////////////////////////////////////
//          0.Release Notes          //
///////////////////////////////////////
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
3. Thumbnail Settings 
	3.1. Fighter Images 
	3.2. Editing Fighters Images Settings
	3.3. Editing Text Settings
4. Others
	4.1. Multi Thumbnail Generation
	4.2. Character Alt Preview
	
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
Tournaments are listed on the following files

	-Tournaments file: settings/tournaments/tournaments.json

To add a new tournament, a new JSON entry must be added with the following
structure:
	
	{
		"id": 
		"name": 
		"logo":
		"foreground":
		"background":
	}
	
Only "background parameter is optional in order to properly generate thumbnails 
as the program will use a default background image if no "background"
is provided.

Here is a "tournament.json" example that contains both Smash or Pass and Alph Apparitions tournaments:

	[
		{
			"id": "sop",
			"name": "Smash or Pass",
			"logo": "assets/tournaments/logos/sop.png",
			"foreground": "assets/tournaments/foregrounds/sop.png"
		},
		{
			"id": "alph",
			"name": "Alph Apparition",
			"logo": "assets/tournaments/logos/alph.png",
			"foreground": "assets/tournaments/foregrounds/alph.png"
		}
	}

NOTE: DO NOT FORGET TO USE COMMAS AND QUOTES AS SHOWN IN THE EXAMPLE



///////////////////////////////////////
//       3. Thumbnail Settings       //
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




----------- 3.3. Editing Text Settings ------------
Thumbnail text settings are stored on the following file:
	- Text Settings: "settings/thumbnails/text/text.json"
	
Settings such as font used, font size, etc. can be ajusted for each available tournament
Here is all available adjustable parameters:

	"id" 								tournamendId reference
	"font" ("BebasNeue-Regular")		font to use
	"bold" (false)						use bold
	"italic" (false)					use italic
	"shadow" (false)					use shadow
	"contour" (0)						add contour of a certain size
	"sizeTop" (0)						font size of text on thumbnail's top portion
	"sizeBottom" (0)					font size of text on thumbnail's bottom portion
	"angleTop" (0)						skew of text on thumbnail's top portion (in º)
	"angleBottom" (0)					skew of text on thumbnail's bottom portion (in º)
	"downOffsetTop" (0)					vertical adjustment of text on thumbnail's top portion
	"downOffsetBottom" (0)				vertical adjustment of text on thumbnail's bottom portion
	
If one of these parameters is missing, a default value will be used, which is shownn in ().
Id does not have a default value as it is used to combine info on "tournaments.json".

Here is an example of text.json that contains both Smash or Pass and Alph Apparitions text settings:
	
	[
		{
			"id": "sop",
			"font": "BebasNeue-Regular",
			"bold": true,
			"italic": true,
			"shadow": true,
			"contour": 0,
			"sizeTop": 90,
			"sizeBottom": 75,
			"angleTop": -2,
			"angleBottom": -2,
			"downOffsetTop": [10, 5],
			"downOffsetBottom": [0, -5]
		},
		{
			"id": "alph",
			"font": "Pikmin-Normal",
			"bold": true,
			"italic": false,
			"shadow": false,
			"contour": 3,
			"sizeTop": 85,
			"sizeBottom": 75,
			"angleTop": -2,
			"angleBottom": -2,
			"downOffsetTop": [0, 0],
			"downOffsetBottom": [-20, -15]
		}
	]
	

///////////////////////////////////////
//             4. Others             //
///////////////////////////////////////


----------- 4.1. Multi Thumbnail Generation ------------
Rules:
- (All parameters must be separated by a ";"
- First line must have tournament id and date (ex.:sop;05/12/2020)

- Each following line contains info for thumbnail generation (1 line = 1 thumbnail)
- 1st comes the players names. Then the characters. Afterwards, the alternate colors.
And finally, the name of the round played by the players
	Ex: Fizbo;Darkout;pikachu;pokemon_trainer;4;5;Winners Semis

- The characters names must be written the exact same way as shown in scale.txt, 
offset.txt and flip.txt
 

------------4.2. Character Alt Preview-------------------
When selecting a character from the dropdown, an icon will appear
This icon is the repective stock icon of the selected character.
Its color also changes when selecting a different alt.

To have a better look at the selected alt, the icon can be clicked
to show a fighter image that will appear on the thumbnail (character and alt).

The majority of the preview images are from the official Smash Bros. Ultimate site.
However, if the user has the respective fighter and alt saved on folder
"assets/fighters/", the program will open this images locally.



