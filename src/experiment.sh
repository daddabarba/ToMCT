#!/bin/bash

#SBATCH --job-name=ToMCT_exp_$(( ( RANDOM % 10000 )  + 1 ))
#SBATCH --mail-type=END
#SBATCH --mail-user=davidebarbieri97@gmail.com
#SBATCH --output=/home/s3090078/job-%j.log

#SBATCH --time=3-00:00:00
#SBATCH --nodes=1
#SBATCH --cpus-per-task=1
#SBATCH --mem-per-cpu=1024

srun ./run.sh $1 $2 $3 $4 4 5 $5 false $6
