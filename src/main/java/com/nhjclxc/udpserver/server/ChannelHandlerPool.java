//package com.nhjclxc.udpserver.server;
//
//
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelId;
//import io.netty.channel.group.ChannelGroup;
//import io.netty.channel.group.DefaultChannelGroup;
//import io.netty.util.concurrent.GlobalEventExecutor;
//
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.stream.Collectors;
//
///**
// * netty信道池，netty信道与用户id要一一对应
// */
//public class ChannelHandlerPool {
//
//    private ChannelHandlerPool() {  }
//
//    /**
//     * 保存当前的所有信道
//     */
//    private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
//
//    /**
//     * 信道与用户关联
//     */
//    private static final Map<String, ChannelId> channelIdMapByToken = new ConcurrentHashMap<>(64); // 支持一个用户多端登录使用
//    private static final Map<Long, List<ChannelId>> channelIdListMapByUserId = new ConcurrentHashMap<>(32); // 支持一个用户多端登录使用
//
//
//    /**
//     * 保存信道到信道池
//     */
//    private static void saveChannel(Channel channel) {
//        Channel channelTemp = channelGroup.find(channel.id());
//        if (null == channelTemp) {
//            channelGroup.add(channel);
//        }
//    }
//
//    /**
//     * 获取所有信道id指定的信道
//     */
//    public static List<Channel> getChannelList(List<ChannelId> ChannelIdList) {
//        if (ChannelIdList == null || ChannelIdList.size() == 0) {
//            return new ArrayList<>();
//        }
//        return ChannelIdList.stream().filter(Objects::nonNull).map(channelGroup::find).filter(Objects::nonNull).collect(Collectors.toList());
//    }
//
//    /**
//     * 客户端链接到netty服务时，保存该用户的信道
//     *
//     * @param channel 信道
//     * @author 罗贤超
//     */
//    public static void saveChannelByToken(String token, Channel channel) {
//        saveChannel(channel);
//        channelIdMapByToken.put(token, channel.id());
//    }
//
//    public static Channel getChannelByToken(String token) {
//        if (channelIdMapByToken.containsKey(token)){
//            ChannelId channelId = channelIdMapByToken.get(token);
//            return channelGroup.find(channelId);
//        }
//        return null;
//    }
//
//    public static void saveChannelByUserId(Long userId, Channel channel) {
//        saveChannel(channel);
//        List<ChannelId> channelByUserId = getChannelByUserId(userId);
//        channelByUserId.add(channel.id());
//        channelIdListMapByUserId.put(userId, channelByUserId);
//    }
//
//    public static List<ChannelId> getChannelByUserId(Long userId) {
//        if (channelIdListMapByUserId.containsKey(userId)){
//            return channelIdListMapByUserId.get(userId);
//        }
//        return new ArrayList<>();
//    }
//
//    /**
//     * 客户端关闭连接时，移除该用户的信道
//     *
//     * @param channel 信道
//     * @author 罗贤超
//     */
//    public static void removeChannel(Channel channel) {
//        // 移除信道池里面的信道
//        boolean flag = channelGroup.remove(channel);
//
//        // 移除用户与信道对应关系
//        // 信道还存在才去移除
//        if (flag){
//            ChannelId removeChannelId = channel.id();
//
//            // 移除指定token里面的信道 userChannelIdMapByToken<String, ChannelId>
//            Iterator<Map.Entry<String, ChannelId>> iteratorByToken = channelIdMapByToken.entrySet().iterator();
//            while (iteratorByToken.hasNext()) {
//                Map.Entry<String, ChannelId> next = iteratorByToken.next();
//                if (next.getValue().equals(removeChannelId)) {
//                    iteratorByToken.remove();
//                    break;
//                }
//            }
//
//            // 移除指定userId里面的信道 userChannelIdListMap<Long, List<ChannelId>>
//            Iterator<Map.Entry<Long, List<ChannelId>>> iteratorByUserId = channelIdListMapByUserId.entrySet().iterator();
//            while (iteratorByUserId.hasNext()) {
//                Map.Entry<Long, List<ChannelId>> next = iteratorByUserId.next();
//                List<ChannelId> channelIdList = next.getValue();
//                for (ChannelId channelId : channelIdList) {
//                    if (channelId.equals(removeChannelId)) {
//                        iteratorByUserId.remove();
//                        break;
//                    }
//                }
//            }
//
//        }
//    }
//
//    /**
//     * 获取信道
//     *
//     * @param id 信道id
//     * @return 信道
//     * @author 罗贤超
//     */
//    public static Channel getChannel(ChannelId id) {
//        return channelGroup.find(id);
//    }
//
//    /**
//     * 获取所有信道
//     */
//    public static List<Channel> getAllChannel() {
//        return new ArrayList<>(channelGroup);
//    }
//
//}
