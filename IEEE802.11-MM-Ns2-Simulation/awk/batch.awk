BEGIN {
    i = 0;
}

$0 {
   data[i] = $2;
   i++;
}


END {

    if ( lines < 100 ) {
        batch = 1; 
        num_batch=lines;
    } else {
        batch = int(lines/100);
        num_batch=100;
    }

    for(j = 0; j<length(data); j++){
       batch_sum[int(j/batch)] += data[j]; 
    }
    
    for(k=0; k<num_batch;k++)
        printf("%d\t%f\t%d\t%d\n",k,batch_sum[k]/batch,batch,lines) >> output
}
