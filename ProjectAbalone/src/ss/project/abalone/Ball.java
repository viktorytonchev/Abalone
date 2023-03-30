package ss.project.abalone;


public class Ball  {

   private Color color;

   public Ball(Color color) {
	      this.color = color;
	   }

   public Ball(Ball b) {
      this.color = b.color;
   }

   public Color getColor() {
      return this.color;
   }

   public void setColor(Color color) {
      this.color = color;
   }


}
