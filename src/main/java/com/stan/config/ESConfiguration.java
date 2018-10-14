package com.stan.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.network.InetAddresses;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {"classpath:es.properties"})
public class ESConfiguration {



    @Value("${elasticsearch.host}")
    private String EsHost;

    @Value("${elasticsearch.port}")
    private int EsPort;

    @Value("${elasticsearch.clustername}")
    private String EsClusterName;

    @Value("${elasticsearch.cluster-nodes}")
    private String EsClusterNode;

    @Bean
    public TransportClient getESClient() {
        // 设置集群名字
        Settings settings = Settings.builder().put("client.transport.ping_timeout", "30s").put("cluster.name", EsClusterName)
                .put("client.transport.sniff", true).put("node.name", EsPort).put("client.transport.sniff", false).build();
        String[] ipstr = EsHost.split(",");
        TransportClient transportClient = new PreBuiltTransportClient(settings);
        try {
            for (int i = 0; i < ipstr.length; i++) {
                transportClient.addTransportAddress(
                        new InetSocketTransportAddress(InetAddresses.forString(ipstr[i]), EsPort));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //   log.info("ElasticSearch client get over ..config:" + "ip：" + ips + " " + "端口：" + port + " ");
        return transportClient;
    }

    private Settings settings() {
        Settings settings = Settings.builder()
                .put("cluster.name", EsClusterName)
                .put("client.transport.ping_timeout", "30s")
                .put("node.name", EsClusterNode)
                .put("client.transport.sniff", false).build();
        return settings;
    }


}