# clear workspace
if [ -d "trace" ]; then
    rm -rf trace/
fi

if [ -d "result" ]; then
    rm -rf result/
fi

if [ -d "log" ]; then
    rm -rf log/
fi

# rebuild workspace

mkdir -p trace/scenario_1/udp
mkdir -p trace/scenario_1/cbr
mkdir -p trace/scenario_2/udp
mkdir -p trace/scenario_2/cbr

mkdir -p result/scenario_1/udp
mkdir -p result/scenario_1/cbr
mkdir -p result/scenario_2/udp
mkdir -p result/scenario_2/cbr

mkdir -p log/scenario_1/udp
mkdir -p log/scenario_1/cbr
mkdir -p log/scenario_2/udp
mkdir -p log/scenario_2/cbr

# start to simulate scenario I
echo "start to simulate scenario I \n"
ns main.tcl 1 4 ad.tcl ad.nam ad.tr >> log/scenario_1/ad.log
ns main.tcl 1 4 hi.tcl hi.nam hi.tr >> log/scenario_1/hi.log
ns main.tcl 1 4 lo.tcl lo.nam lo.tr >> log/scenario_1/lo.log
echo "finish simulating scenario I \n"

# start to analysis scenario I trace file
echo "start to analysis scenario I trace file\n"

sh analysis.sh udp ad 1
sh analysis.sh udp hi 1
sh analysis.sh udp lo 1

sh analysis.sh cbr ad 1
sh analysis.sh cbr hi 1
sh analysis.sh cbr lo 1

echo "finish analysing scenario I trace file\n"

# move nam and tr file to corresponding directory
mv *.nam trace/scenario_1
mv *.tr trace/scenario_1

node_num=70
#sh setdest.sh $node_num

# start to simulate scenario II
echo "start to simulate scenario II\n"
ns main.tcl 2 $node_num ad.tcl ad.nam ad.tr move.tcl >> log/scenario_2/ad.log
ns main.tcl 2 $node_num hi.tcl hi.nam hi.tr move.tcl >> log/scenario_2/hi.log
ns main.tcl 2 $node_num lo.tcl lo.nam lo.tr move.tcl >> log/scenario_2/lo.log
echo "finish simulating scenario II\n"

echo "start to analysis scenario II trace file\n"

sh analysis.sh udp ad 2
sh analysis.sh udp hi 2
sh analysis.sh udp lo 2

sh analysis.sh cbr ad 2
sh analysis.sh cbr hi 2
sh analysis.sh cbr lo 2

echo "finishing analysing scenario II trace file\n"

# move nam and tr file to corresponding directory
mv *.nam trace/scenario_2
mv *.tr trace/scenario_2

echo "finish all job\n"
