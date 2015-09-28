package com.main;



import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;


import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.ClassLoadingGaugeSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadDump;
import com.metrics.ZabbixReporter;
import com.sender.ZabbixSender;

import java.net.InetAddress;

public class Main {
	static final MetricRegistry metrics = new MetricRegistry();
	
	public static void main(String args[]) throws IOException,
			InterruptedException {
		ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
				.convertRatesTo(TimeUnit.SECONDS)
				.convertDurationsTo(TimeUnit.MILLISECONDS).build();
		metrics.register("jvm.mem", new MemoryUsageGaugeSet());
		metrics.register("jvm.gc", new GarbageCollectorMetricSet());
		metrics.register("jvm.cl", new ClassLoadingGaugeSet());
		reporter.start(5, TimeUnit.SECONDS);


		//get zabbix host
		InetAddress gethostName = InetAddress.getLocalHost();
		String hostName = gethostName.getHostName();
		System.out.println(hostName);
		
		//String hostName = "te2.oss-hub.uk3.ribob01.net";
		//zabbix server
		ZabbixSender zabbixSender = new ZabbixSender("te1.oss-hub.uk3.ribob01.net", 10051);
		ZabbixReporter zabbixReporter = ZabbixReporter.forRegistry(metrics)
				.hostName(hostName).build(zabbixSender);

		zabbixReporter.start(1, TimeUnit.SECONDS);
		System.out.println("Connection successful");
		
		TimeUnit.SECONDS.sleep(500);

	}
	
}