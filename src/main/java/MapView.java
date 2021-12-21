public class MapView {
    IWorldMap map;
    int width;
    int height;
    Vector2d jngMin;
    Vector2d jngMax;
    public MapView(IWorldMap map, InitialParameters initialParameters){
        this.map = map;
        this.width = initialParameters.mapWidth;
        this.height = initialParameters.mapHeight;
        this.jngMin = initialParameters.jungleLowerLeft;
        this.jngMax = initialParameters.jungleUpperRight;
    }

    private boolean isJng(Vector2d a){
        if((a.x>=jngMin.x && a.x<=jngMax.x && a.y>= jngMin.y && a.y <=jngMax.y)){
            return true;
        }

        return false;

    }

    public void out(){
        System.out.println(new Vector2d(this.width, this.height));
        System.out.println(this.jngMin);
        System.out.println(this.jngMax);
        System.out.println("");
        for(int i = 0; i < this.height; i++){
            for (int j = 0; j < this.width; j++){

                String res = "";

                if(isJng(new Vector2d(j, i))){
                    res += "+";
                }
                else{
                    res +="-";
                }
                if(map.animalsAt(new Vector2d(j,i))!=null && map.animalsAt(new Vector2d(j,i)).size()>0){
                    res += "Y";
                }
                else{
                    res+=" ";
                }
                if(map.grassAt(new Vector2d(j,i))!=null){
                    res+="*";
                }
                else{
                    res+=" ";
                }
                res+="";
                System.out.print(res);
            }
            System.out.println("");
        }
    }
    public String toString(){
        out();
        return "";
    }
}
