This is a bad clone of mario using AndEngine, and its Box2D and TMX extensions

Bugs/Things not working yet:
	-horizontal line flickering in the background
	-mario's fastest movement is glitchy - because of bad velocity capping?
	-mario can jump if any part of his body touches a wall/block - cant get a foot sensor working
	-except for the floor, mario gets stuck on blocks lined up 
		* the floor is one big rectangle to fix this, haven't implemented it on random boxes yet
Things that are working/should be:
	-Mario movement animations(walk,run,skid,jump)
	-Coin animation && adding to the score
	-tmx map with 3 layers (background,objects,foreground)
	-jump and coin sounds (can get very annoying tho)

