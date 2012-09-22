BEGIN{
}

$0{
    data[FNR] = $2
    sum += $2;    
    num += 1;
 }

END{
    ave = sum/num;

    for(i = 0; i<num; i++){
        var += (data[i] - ave) * (data[i] - ave)
    }
    
    printf("average " forwhat ": %f\n",ave) >> summaryfile; 
    printf(forwhat " standard deviation: %f\n", sqrt(var/num)) >> summaryfile;

}
