package github.kutouzi.actassistant.map;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import github.kutouzi.actassistant.service.BaidujisuService;
import github.kutouzi.actassistant.service.DouyinjisuService;
import github.kutouzi.actassistant.service.KuaishoujisuService;
import github.kutouzi.actassistant.service.MeituanService;
import github.kutouzi.actassistant.service.NullService;
import github.kutouzi.actassistant.service.PinduoduoService;
import github.kutouzi.actassistant.service.XiaohongshuService;

public class PackageNameIndexMap {
    // TODO: 将这些分离成各个类
    public static final Map<Integer,String> packageNameIndexMap = Stream.of(
            new AbstractMap.SimpleEntry<>(NullService.APPLICATION_INDEX,""),
            new AbstractMap.SimpleEntry<>(PinduoduoService.APPLICATION_INDEX, PinduoduoService.PACKAGE_NAME),
            new AbstractMap.SimpleEntry<>(MeituanService.APPLICATION_INDEX,MeituanService.PACKAGE_NAME),
            new AbstractMap.SimpleEntry<>(DouyinjisuService.APPLICATION_INDEX,DouyinjisuService.PACKAGE_NAME),
            new AbstractMap.SimpleEntry<>(KuaishoujisuService.APPLICATION_INDEX,KuaishoujisuService.PACKAGE_NAME),
            new AbstractMap.SimpleEntry<>(BaidujisuService.APPLICATION_INDEX,BaidujisuService.PACKAGE_NAME),
            new AbstractMap.SimpleEntry<>(XiaohongshuService.APPLICATION_INDEX,XiaohongshuService.PACKAGE_NAME)
    ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
}
