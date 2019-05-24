/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acrored_ipsc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/**
 *
 * @author victor
 */
public class NetworkSpeed {
    
    public static Sigar sigar;
    static Map<String, Long> rxCurrentMap = new HashMap<String, Long>();
    static Map<String, List<Long>> rxChangeMap = new HashMap<String, List<Long>>();
    static Map<String, Long> txCurrentMap = new HashMap<String, Long>();
    static Map<String, List<Long>> txChangeMap = new HashMap<String, List<Long>>();
    
    public String networkspeed_download;
    public String networkspeed_upload;
    
    public NetworkSpeed(Sigar s) throws SigarException, InterruptedException {
        sigar = s;
        getMetric();
        //newMetricThread();
        Thread.sleep(1000);
    }
    
    private static long getMetricData(Map<String, List<Long>> rxChangeMap) {
        long total = 0;
        for (Map.Entry<String, List<Long>> entry : rxChangeMap.entrySet()) {
            int average = 0;
            for (Long l : entry.getValue()) {
                average += l;
            }
            total += average / entry.getValue().size();
        }
        return total;
    }

    private static void saveChange(Map<String, Long> currentMap,Map<String, List<Long>> changeMap, String hwaddr, long current, String ni) {
        Long oldCurrent = currentMap.get(ni);
        if (oldCurrent != null) {
            List<Long> list = changeMap.get(hwaddr);
            
            if (list == null) {
                list = new LinkedList<Long>();
                changeMap.put(hwaddr, list);
            }
            list.add((current - oldCurrent));
        }
        currentMap.put(ni, current);
    }  
    
    public Long[] getMetric() throws SigarException, InterruptedException {
        for (String ni : sigar.getNetInterfaceList()) {
            // System.out.println(ni);
            NetInterfaceStat netStat = sigar.getNetInterfaceStat(ni);
            NetInterfaceConfig ifConfig = sigar.getNetInterfaceConfig(ni);
            String hwaddr = null;
            if (!NetFlags.NULL_HWADDR.equals(ifConfig.getHwaddr())) {
                hwaddr = ifConfig.getHwaddr();
            }
            if (hwaddr != null) {
                long rxCurrenttmp = netStat.getRxBytes();
                saveChange(rxCurrentMap, rxChangeMap, hwaddr, rxCurrenttmp, ni);
                long txCurrenttmp = netStat.getTxBytes();
                saveChange(txCurrentMap, txChangeMap, hwaddr, txCurrenttmp, ni);
            }
        }
        long totalrx = getMetricData(rxChangeMap);
        long totaltx = getMetricData(txChangeMap);
        for (List<Long> l : rxChangeMap.values()) {
            l.clear();
        }
        for (List<Long> l : txChangeMap.values()) {
            l.clear();
        }
        return new Long[]{totalrx, totaltx};
    }
    
    public  void newMetricThread() throws SigarException, InterruptedException {
        while (true) {
            Long[] m = getMetric();
            long totalrx00 = m[0];
            long totaltx00 = m[1];

            networkspeed_download = Sigar.formatSize(totalrx00);
            networkspeed_upload =  Sigar.formatSize(totaltx00);
            
            System.out.print("totalrx(download): ");
            System.out.println("\t" + networkspeed_download);
            System.out.print("totaltx(upload): ");
            System.out.println("\t" + networkspeed_upload);
            System.out.println("*******************************");
            Thread.sleep(1000);
        }

    }
    
    
}
