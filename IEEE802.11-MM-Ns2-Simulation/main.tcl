proc getopt {argc argv} {

        global opt

        lappend optlist nn

        for {set i 0} {$i < $argc} {incr i} {

            set opt($i) [lindex $argv $i];

        }

}

getopt $argc $argv
set val(scenario) $opt(0)
set val(numnodes) $opt(1)
set val(type) $opt(2)
set val(namfile) $opt(3)
set val(tracefile) $opt(4)

if { $val(scenario) == 2 } {
    set val(movefile) $opt(5)
}

set tip ""
append tip "Load config file:"
append tip $val(type)
puts $tip

proc finish {} {
    global ns_ nf tracefd
    $ns_ flush-trace
    close $nf
    close $tracefd
    
    exit 0
}

set val(chan) [new Channel/WirelessChannel]
set val(prop) Propagation/TwoRayGround
set val(netif) Phy/WirelessPhy ;
set val(mac) Mac/802_11
set val(ifq) Queue/DropTail/PriQueue ;# interface queue type
set val(ll) LL
set val(ant) Antenna/OmniAntenna ;# antenna model
set val(ifqlen) 50
set val(nn) $val(numnodes)
set val(rp) AODV

set ns_ [new Simulator]
#puts "random [ns-random 0]"

set tracefd [open $val(tracefile) w]
$ns_ trace-all $tracefd

set nf [open $val(namfile) w]
$ns_ namtrace-all-wireless $nf 350 350

Agent/AODV set ignorehdr_ 1

set topo [new Topography]
$topo load_flatgrid 350 350
set god_ [create-god $val(nn)]

# Pt = 7.21505664 * (d^4) * e-11
# ./threshold -Pt 0.000935073 -fr 2.472e9 -m TwoRayGround 60
Phy/WirelessPhy set CPThresh_ 10.0
Phy/WirelessPhy set CSThresh_ 5.00522e-12
Phy/WirelessPhy set RXThresh_ 2.42253e-11
Phy/WirelessPhy set Pt_ 0.000935073
Phy/WirelessPhy set freq_ 2.472e9 
Phy/WirelessPhy set L_ 1.0 

Mac/802_11 set CWMin_ 31
Mac/802_11 set CWMax_ 1023
Mac/802_11 set SlotTime_ 0.000020
Mac/802_11 set SIFS_ 0.000010
Mac/802_11 set PreambleLength_ 144
Mac/802_11 set PLCPHeaderLength_ 48
Mac/802_11 set PLCPDataRate_ 1.0e6
Mac/802_11 set dataRate_ 11.0e6
Mac/802_11 set basicRate_ 1.0e6

Mac/802_11 set RTSThreshold_ 256
Mac/802_11 set ShortRetryLimit_ 7
Mac/802_11 set LongRetryLimit_ 4

$ns_ node-config -adhocRouting $val(rp) \
                -llType $val(ll) \
                -macType $val(mac) \
                -ifqType $val(ifq) \
                -ifqLen $val(ifqlen) \
                -antType $val(ant) \
                -propType $val(prop) \
                -phyType $val(netif) \
                -channel $val(chan) \
                -topoInstance $topo \
                -agentTrace ON \
                -routerTrace ON \
                -macTrace ON \
                -movementTrace OFF

if { $val(scenario) == 1} {
    for {set i 0} {$i < $val(nn)} {incr i} {
        set node_($i) [$ns_ node]
        $node_($i) random-motion 0
        $node_($i) set X_ [expr 25 + 50*$i]
        $node_($i) set Y_ 175
        $node_($i) set Z_ 0
        $ns_ initial_node_pos $node_($i) 15
    }
} else {
    for {set i 0} {$i < $val(nn)} {incr i} {
        set node_($i) [$ns_ node]
        $node_($i) random-motion 0
    }
    
    source $val(movefile)
    
    for {set i 0} {$i < $val(nn)} {incr i} {
        $ns_ initial_node_pos $node_($i) 15
    }

}


# setup a MM UDP connection
set udp_s [new Agent/UDP/UDPmm]
set udp_r [new Agent/UDP/UDPmm]
$ns_ attach-agent $node_(0) $udp_s
$ns_ attach-agent $node_(3) $udp_r
$ns_ connect $udp_s $udp_r
$udp_s set packetSize_ 1000
$udp_r set packetSize_ 1000
$udp_s set fid_ 1
$udp_r set fid_ 1

#setup a MM Application
set qoas_s [new Application/QOASServer]
set qoas_r [new Application/QOASClient]

source $val(type)

$qoas_s attach-agent $udp_s
$qoas_r attach-agent $udp_r
$qoas_s set pktsize_ 1000
$qoas_s set random_ false


set udpcbrs [new Agent/UDP]
$udpcbrs set fid_ 2
$udpcbrs set packetsize_ 1000
$ns_ attach-agent $node_(0) $udpcbrs

set udpcbrr [new Agent/UDP]
$ns_ attach-agent $node_(3) $udpcbrr
$ns_ connect $udpcbrs $udpcbrr


set cbr [new Application/Traffic/CBR]
$cbr attach-agent $udpcbrs
$cbr set type_ CBR
$cbr set packet_size_ 1000
$cbr set rate_ 0.5mb
$cbr set random_ false


$ns_ at 1.0 "$cbr start"
$ns_ at 1.0 "$qoas_s start"
$ns_ at 100 "finish"
puts "Starting Simulation..."

$ns_ run

