package ss.project.abalone;


/**
 * The color of the ball. Module 2 project Abalone.
 *
 * @author Dimitar Popov and Viktor Tonchev
 * @version $Revision: 1.0 $
 */

public enum Color {

   /** A While Ball */
   YELLOW,
   /** A Blue Ball */
   BLUE,
   /** A Black Ball */
   BLACK,
   /** A Red Ball */
   RED;

    /**
     * Returns true if the color is a player color.
     */

	public Boolean isPlayerColor() {
      return (this == Color.YELLOW || this == Color.BLACK|| this == Color.BLUE|| this == Color.RED);
   }

    /**
     * Returns true if the color other is a color of the same team.
     */
   public Boolean isSameTeamColor(Color other) {
	   //TODO check the team colors
	   return (this == Color.BLACK && other == Color.YELLOW ) ||
			  (this == Color.YELLOW && other == Color.BLACK ) ||
			  (this == Color.RED && other == Color.BLUE ) ||
			  (this == Color.BLUE && other == Color.RED );
   }
   /**
    * Returns color by the index in the Enum
    * @param index the index in the enum
    * @return the color at the index if index is between 0 and 3 or YELLOW otherwise
    */
   public static Color getbyIndex( int index ) {

	   Color color = Color.YELLOW;
	   
	   switch (index) {
	   case 0:
		   color = Color.YELLOW;
		   break;
	   case 1:
		   color = Color.BLUE;
		   break;
	   case 2:
		   color = Color.BLACK;
		   break;
	   case 3:
		   color = Color.RED;
		   break;

	   default:
		   color = Color.YELLOW;
		   break;
	   }
	   
	   return color;
   }
}
