BEGIN{
    i = 0;
}

$0{
    psnr = $8
    rate = $14
    if($7 == "PSNR:"){
        printf("%d\t%f\n",i,psnr) >> psnrfile;
        printf("%d\t%f\n",i,substr(rate,0,4)) >> ratefile;
        i = i + 1;
    }
}

END{


}
