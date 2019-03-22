for ORDER1 in 0 1
do
	for ORDER2 in 0 1
	do
		for LR1 in $(seq 0.1 0.2 1.0)
		do
			for LR2 in $(seq 0.1 0.2 1.0)
			do
				./run.sh $ORDER1 $ORDER2 $LR1 $LR2 4 5 $1 false $2 &
			done
		done
	done
done
