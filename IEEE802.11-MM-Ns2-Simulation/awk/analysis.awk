BEGIN {
    highest_pkt_id = 0;
    total_delay = 0;
    send_pkt = 0;
    recv_pkt = 0;
    drop_pkt = 0;
    i = 0;
    begin_time = -1;
}

$0 {
    layer = $4;
    pkt_type = $7;

    if(layer == "MAC" && pkt_type == application) {
        action = $1;
        time = $2;
        pkt_id = $6;
        pkt_size = $8;

        crt_node_id = $3;
        
        if( action=="s" && crt_node_id == "_0_" ) 
        {
            send_pkt += 1;

            if( pkt_id > highest_pkt_id)
                highest_pkt_id = pkt_id

            if( start_time[pkt_id] == 0 )
                start_time[pkt_id] = time;
        }
            
        
        if( action=="r" && crt_node_id == "_3_" )
        {
            recv_pkt += 1;
            
            pkt_size_sum[i+1] = pkt_size_sum[i] + pkt_size;

            if(begin_time == -1)  begin_time = time

            end_time[pkt_id] = time;
            
            over_time[i] = time;
            i = i+1;
        }
    }
}


END {

    last_seqno = 0;
    last_delay = 0;
    seqno_diff = 0;

    for (pkt_id = 0; pkt_id <= highest_pkt_id;pkt_id++)
    {
        start = start_time[pkt_id];
        end = end_time[pkt_id];
        duration = end - start;
        
        if(start < end) 
        {
            total_delay += duration;
            printf("%d\t%f\n", pkt_id, duration) >> delayfile;
        
            
            seqno_diff = pkt_id - last_seqno;
            delay_diff = duration - last_delay;

            if ( seqno_diff == 0) 
                jitter = 0;
            else
                jitter = delay_diff / seqno_diff;

            printf("%d\t%f\n",pkt_id,jitter) >> jitterfile;
            last_seqno=pkt_id;
            last_delay=duration;
        }
    }
    
    for(j=2;j<i;j++)
    {
        throughput = pkt_size_sum[j]/(over_time[j]-begin_time)*8/1000;
        printf("%.2f\t%.2f\n",over_time[j],throughput) >> throughputfile;
    }

    printf("send %d packets,receive %d packets,loss %d packets,loss rate: %f\n",
            send_pkt,recv_pkt,
            send_pkt-recv_pkt,
            (send_pkt-recv_pkt)/send_pkt) >> summaryfile;
    

    printf("recv_pkt: %d, average delay: %f\n",
           recv_pkt,
           total_delay/recv_pkt) >> summaryfile;

}
