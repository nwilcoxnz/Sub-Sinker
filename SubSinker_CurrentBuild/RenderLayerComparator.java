import java.util.*;

public class RenderLayerComparator implements Comparator<GameObject> {
    @Override
    public int compare(GameObject a, GameObject b) {
        return a.myRenderLayer > b.myRenderLayer ? -1 : a.myRenderLayer == b.myRenderLayer ? 0 : 1;
    }
}