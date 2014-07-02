package pl.edu.agh.yapa.model;

import java.util.*;

/**
 * Created by Dominik on 02.07.2014.
 */
public class AdSnapshot {
    private Date date;
    private List<Ad> ads;

    public AdSnapshot(Date date, List<Ad> ads) {
        this.date = date;
        this.ads = ads;
    }

    public List<Ad> getAds() {
        return ads;
    }

    public void setAds(List<Ad> ads) {
        this.ads = ads;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public static List<AdSnapshot> groupBySnapshots(List<Ad> ads) {
        Map<Date, List<Ad>> map = new HashMap<>();
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
        for (Map.Entry<Date, List<Ad>> entry : map.entrySet()) {
            snapshots.add(new AdSnapshot(entry.getKey(), entry.getValue()));
        }
        return snapshots;
    }
}
