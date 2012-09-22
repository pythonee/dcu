gawk -v application=$1 \
     -v throughputfile=result/scenario_$3/$1/$2.throughput \
     -v delayfile=$2.delay \
     -v jitterfile=$2.jitter \
     -v summaryfile=result/scenario\_$3/$1/summary \
     -f awk/analysis.awk $2.tr

gawk -v psnrfile=$2.psnr \
     -v ratefile=$2.rate \
     -f awk/log.awk log/scenario_$3/$2.log 

gawk -v summaryfile=result/scenario_$3/$1/summary \
     -v forwhat=throughput \
     -f awk/stdev.awk result/scenario_$3/$1/$2.throughput

gawk -v summaryfile=result/scenario_$3/$1/summary \
     -v forwhat=delay \
     -f awk/stdev.awk $2.delay

gawk -v summaryfile=result/scenario_$3/$1/summary \
     -v forwhat=PSNR \
     -f awk/stdev.awk $2.psnr

gawk -v output=result/scenario_$3/$1/$2.delay \
     -v lines=`sed -n '$=' $2.delay` \
     -f awk/batch.awk $2.delay

gawk -v output=result/scenario_$3/$1/$2.jitter \
     -v lines=`sed -n '$=' $2.jitter` \
     -f awk/batch.awk $2.jitter

gawk -v output=result/scenario_$3/$1/$2.psnr \
     -v lines=`sed -n '$=' $2.psnr` \
     -f awk/batch.awk $2.psnr

gawk -v output=result/scenario_$3/$1/$2.rate \
     -v lines=`sed -n '$=' $2.rate` \
     -f awk/batch.awk $2.rate

rm $2.delay $2.jitter $2.psnr $2.rate
