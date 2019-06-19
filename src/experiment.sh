#!/bin/bash

#SBATCH --mail-type=END
#SBATCH --mail-user=davidebarbieri97@gmail.com
#SBATCH --output=/home/s3090078/results/job-%j.log

#SBATCH --time=7-03:00:00
#SBATCH --nodes=1
#SBATCH --cpus-per-task=1
#SBATCH --mem-per-cpu=1024
#SBATCH --array=0-728

order=({0..2})
ls=($(seq 0.1 0.1 0.9))

i=$SLURM_ARRAY_TASK_ID
ls2=$(($i%9))

i=$i/9
ls1=$(($i%9))

i=$i/9
o2=$(($i%3))

i=$i/3
o1=$(($i%3))


srun ./run.sh $o1 $o2 $ls1 $ls2 4 5 $1 false
