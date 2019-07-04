#!/bin/bash

i=$(squeue -u $USER | wc -l)
j=$(python3 extract.py /home/s3090078/results/ | grep failed | wc -l)
echo $((j-i))
