package ss.project.abalone;

/**
 * Enum for direction. Module 2 project Abalone.
 *
 * @author Dimitar Popov and Viktor Tonchev
 * @version $Revision: 1.0 $
 */
public enum Direction {

   UPLEFT,
   UPRIGHT,
   LEFT,
   RIGHT,
   DOWNLEFT,
   DOWNRIGHT,
   INVALID;

   public Direction oppositeDirection()
   {
      switch ( this )
      {
          case DOWNLEFT:
             return UPLEFT;
          case DOWNRIGHT:
             return UPRIGHT;
          case UPLEFT:
             return DOWNLEFT;
          case UPRIGHT:
             return DOWNRIGHT;
          default:
             return this;
      }
   }
   
   public static Direction stringToDirection(String strDirection) {
	   Direction dir = Direction.INVALID;
	   
	   if ( strDirection.equals("UL") ) {
		   dir = Direction.UPLEFT;
	   } else if ( strDirection.equals("UR") ) {
		   dir = Direction.UPRIGHT;
	   } else if ( strDirection.equals("DL") ) {
		   dir = Direction.DOWNLEFT;
	   } else if ( strDirection.equals("DR") ) {
		   dir = Direction.DOWNRIGHT;
	   } else if ( strDirection.equals("L") ) {
		   dir = Direction.LEFT;
	   } else if ( strDirection.equals("R") ) {
		   dir = Direction.RIGHT;
	   }
		   
		return dir;   
   }
}
