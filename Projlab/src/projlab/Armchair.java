package projlab;



public class Armchair implements Element, Steppable{
    
    private int countToDrop;
    private Tile tile;
    private SleepyPanda panda;
    
    //Alapértelmezett Konstruktor
    public Armchair(){}
    
    //Konstruktor, megadhatjuk neki azt, hogy melyik mezőn tartózkodik
    public Armchair(Tile tl){
        tile = tl;
        for(Tile negh : tl.getNeighbors()){
            negh.addNeighborChair(this);
        }
    }
    
    //Ez a metódus beállítja a tile értékét a paraméterben megadottra
    public void setTile(Tile tl){
        tile = tl;
        for(Tile negh : tl.getNeighbors()){
            negh.addNeighborChair(this);
        }
    }
    
    /**
     * Ez a metódus kidobja a pandát az Armchairből.
     * 
     */
    public void drop() {
            panda.setStatus(true);
            System.out.println("kidobtam");
            tile.placeMoveableOnNeighbor(panda);
            panda=null;
    }

    /**
     * Ez a metódus beleteszi a SleepyPandát az 
     
     be, amelyik bele szeretne menni.
     * 
     * @param sp Az a panda amelyik aludni szeretne.
     */
    public void gyereRam(SleepyPanda sp) {
        sp.breakOut();
        if(panda == null){
            countToDrop = 10;
            Tile tmp = new Tile();
            sp.setTile(tmp);
            tmp.setElement(sp);
            panda = sp;
            System.out.println("SleepyPanda elaludt.");
        }
    }
    
    //Ez az Armchairt léptető metódus
    @Override
    public void step() {
            if(countToDrop > 0  && panda != null) countToDrop--;
            else if(countToDrop < 1 && panda == null);
            else if(countToDrop < 1 && panda != null) drop();
    }
    
    /**
     * @return boolean szabad-e az Armchair
     */
    public boolean isFree(){       
        return panda == null;
    }

    @Override
    public void hitBy(Orangutan o) {  }

    @Override
    public void hitBy(Panda p) {  }

    @Override
    public void collideWith(Element e) { }
}
