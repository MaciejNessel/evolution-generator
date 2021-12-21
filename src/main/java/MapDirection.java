public enum MapDirection {
    NORTH, SOUTH, WEST, EAST, NORTHWEST, SOUTHWEST, NORTHEAST, SOUTHEAST;

    public String toString(){
        return switch (this){
            case EAST -> "E";
            case WEST -> "W";
            case NORTH -> "N";
            case SOUTH -> "S";
            case NORTHEAST -> "NE";
            case SOUTHEAST -> "SE";
            case NORTHWEST -> "NW";
            case SOUTHWEST -> "SW";
        };
    }

    public Vector2d toVector(){
        return switch (this){
            case EAST -> new Vector2d(1,0);
            case WEST -> new Vector2d(-1,0);
            case NORTH -> new Vector2d(0,1);
            case SOUTH -> new Vector2d(0,-1);
            case NORTHEAST -> new Vector2d(1,1);
            case SOUTHEAST -> new Vector2d(1,-1);
            case NORTHWEST -> new Vector2d(-1,1);
            case SOUTHWEST -> new Vector2d(-1,-1);
        };
    }

    public MapDirection oneChangeDirection(){
                return switch (this){
                case EAST -> SOUTHEAST;
                case WEST -> NORTHWEST;
                case NORTH -> NORTHEAST;
                case SOUTH -> SOUTHWEST;
                case NORTHEAST -> EAST;
                case SOUTHEAST -> SOUTH;
                case NORTHWEST -> NORTH;
                case SOUTHWEST -> WEST;
        };
    }

    static public MapDirection numberToDirection(int num){
        return switch (num){
            case 0 -> MapDirection.SOUTHEAST;
            case 1 -> MapDirection.NORTHWEST;
            case 2 -> MapDirection.NORTHEAST;
            case 3 -> MapDirection.SOUTHWEST;
            case 4 -> MapDirection.EAST;
            case 5 -> MapDirection.SOUTH;
            case 6 -> MapDirection.NORTH;
            case 7 -> MapDirection.WEST;
            default -> throw new IllegalStateException("Unexpected value: " + num);
        };
    }



}
