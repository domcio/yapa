package pl.edu.agh.yapa.model;

import java.util.*;

/**
 * Created by Dominik on 02.07.2014.
 */
public class AdSnapshot {
    private SnapshotStamp stamp;
    private List<Ad> ads;

    public AdSnapshot(SnapshotStamp stamp, List<Ad> ads) {
        this.stamp = stamp;
        this.ads = ads;
    }

    public List<Ad> getAds() {
        return ads;
    }

    public void setAds(List<Ad> ads) {
        this.ads = ads;
    }

    public SnapshotStamp getStamp() {
        return stamp;
    }

    public void setStamp(SnapshotStamp stamp) {
        this.stamp = stamp;
    }

    public static List<AdSnapshot> groupBySnapshots(List<Ad> ads) {
        Map<SnapshotStamp, List<Ad>> map = new HashMap<>();
        for (Ad ad : ads) {
            if (!map.containsKey(ad.getSnapshot())) {
                List<Ad> adList = new ArrayList<>();
                adList.add(ad);
                map.put(ad.getSnapshot(), adList);
            } else {
                List<Ad> list = map.get(ad.getSnapshot());
                list.add(ad);
            }
        }
        List<AdSnapshot> snapshots = new ArrayList<>();
        for (Map.Entry<SnapshotStamp, List<Ad>> entry : map.entrySet()) {
            snapshots.add(new AdSnapshot(entry.getKey(), entry.getValue()));
        }
        return snapshots;
    }
}
