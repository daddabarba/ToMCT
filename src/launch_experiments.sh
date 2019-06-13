for ORDER1 in 0 1 2
do
    for ORDER2 in 0 1 2
    do
        for LR1 in $(seq 0.1 0.1 0.9)
        do
            for LR2 in $(seq 0.1 0.1 0.9)
            do
                sbatch ./experiment.sh $ORDER1 $ORDER2 $LR1 $LR2 $1 $2
            done
        done
    done
done

