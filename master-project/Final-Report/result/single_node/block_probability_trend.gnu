set key box
set key top right
set xtics nomirror
set ytics nomirror
set datafile separator ","
set grid 
set xlabel "Simulation Time [seconds]"
set ylabel "Burst Blocking Probability"
set terminal postscript eps enhanced color
set output "block_probability_trend.eps"
plot "with_cc_0_0_6/block_probability.csv" using 1:6 with lines lw 3 title "network load scale 0.5" ,\
"with_cc_0_0_6/block_probability.csv" using 1:7 with lines lw 3 title "network load scale 0.6" ,\
"with_cc_0_0_6/block_probability.csv" using 1:8 with lines lw 3 title "network load scale 0.7" ,\
"with_cc_0_0_6/block_probability.csv" using 1:9 with lines lw 3 title "network load scale 0.8"
