#!/bin/bash

output="simulation_results_100.csv"
echo "Nodes,PacketsPerSecond,Speed,Throughput,EndToEndDelay,PacketDeliveryRatio,PacketDropRatio" > $output

for nodes in 100; do
    for rate in 100 200 300 400; do
        for speed in 5 10 15 20; do
            ./ns3 run scratch/manet-routing-compare.cc -- --numNodes=$nodes --packetsPerSecond=$rate --nodeSpeed=$speed --CSVfileName=$output
        done
    done
done

# output="simulation_results.csv"
# echo "Nodes,PacketsPerSecond,Speed,Throughput,EndToEndDelay,PacketDeliveryRatio,PacketDropRatio" > $output

# ./ns3 run scratch/manet-routing-compare.cc -- --numNodes=20 --packetsPerSecond=100 --nodeSpeed=5 --CSVfileName=$output
# ./ns3 run scratch/manet-routing-compare.cc -- --numNodes=20 --packetsPerSecond=200 --nodeSpeed=5 --CSVfileName=$output
# ./ns3 run scratch/manet-routing-compare.cc -- --numNodes=20 --packetsPerSecond=300 --nodeSpeed=5 --CSVfileName=$output
# ./ns3 run scratch/manet-routing-compare.cc -- --numNodes=20 --packetsPerSecond=400 --nodeSpeed=5 --CSVfileName=$output

# ./ns3 run scratch/manet-routing-compare.cc -- --numNodes=20 --packetsPerSecond=100 --nodeSpeed=5 --CSVfileName=$output
# ./ns3 run scratch/manet-routing-compare.cc -- --numNodes=20 --packetsPerSecond=100 --nodeSpeed=10 --CSVfileName=$output
# ./ns3 run scratch/manet-routing-compare.cc -- --numNodes=20 --packetsPerSecond=100 --nodeSpeed=15 --CSVfileName=$output
# ./ns3 run scratch/manet-routing-compare.cc -- --numNodes=20 --packetsPerSecond=100 --nodeSpeed=20 --CSVfileName=$output

# ./ns3 run scratch/manet-routing-compare.cc -- --numNodes=20 --packetsPerSecond=100 --nodeSpeed=5 --CSVfileName=$output
# ./ns3 run scratch/manet-routing-compare.cc -- --numNodes=30 --packetsPerSecond=100 --nodeSpeed=5 --CSVfileName=$output
# ./ns3 run scratch/manet-routing-compare.cc -- --numNodes=60 --packetsPerSecond=100 --nodeSpeed=5 --CSVfileName=$output
# ./ns3 run scratch/manet-routing-compare.cc -- --numNodes=80 --packetsPerSecond=100 --nodeSpeed=5 --CSVfileName=$output



