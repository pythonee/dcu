rm move.tcl
path=~/ns-allinone-2.34/ns-2.34/indep-utils/cmu-scen-gen/setdest

maxspeed=10
minspeed=2
numnodes=$1
maxx=350
maxy=350
simtime=100
pt=2
version=2

$path"/setdest" -n $numnodes \
                -v $version \
                -p $pt \
                -M $maxspeed \
                -m $minspeed \
                -t $simtime \
                -x $maxx \
                -y $maxy \
                >> move.tcl
