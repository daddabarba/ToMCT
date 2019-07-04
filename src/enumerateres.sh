for o1 in {0..2}
do
	for o2 in {0..2}
	do
		echo "$o1-$o2 $(python3 extract.py ../../results/ | grep "($o1, $o2" | wc -l)"
	done
done
