import os
import sys
import functools as ftools

import json

def getFiles(path=os.getcwd()):
	return [os.path.join(path,f) for f in os.listdir(path) if os.path.isfile(os.path.join(path,f))]

def getPar(data, agent, par):
	return data['players'][0][agent]['ToM'][par]

def getRes(file):

	with open(file) as f:
		line = f.readline()

	data = json.loads(line[0:len(line)-1])

	return getPar(data,0,'order'), getPar(data,1,'order'), getPar(data,0,'learningSpeed'), getPar(data,1,'learningSpeed'), data['games']

def refactorData(path=os.getcwd()):

	files = getFiles(path)

	refactored = {}

	for file in files:

		try:
			or0, or1, ls0, ls1, vals = getRes(file)
			if (or0, or1, ls0, ls1) in refactored.keys():
				print('\033[92m', "experiment ", (or0, or1, ls0, ls1) , " duplicate", '\033[0m')
			else:
				refactored[(or0, or1, ls0, ls1)] = vals
		except:
			print('\033[91m', "experiment ", file, " failed", '\033[0m')

	return len(files), refactored

if __name__ == "__main__":

	def compare(v1, v2):

		if v1[0] != v2[0]:
			return -1 if v1[0]<v2[0] else 1

		if v1[1] != v2[1]:
			return -1 if v1[1]<v2[1] else 1

		if v1[2] != v2[2]:
			return -1 if v1[2]<v2[2] else 1

		if v1[3] != v2[3]:
			return -1 if v1[3]<v2[3] else 1

		return 0

	tot, data = refactorData(sys.argv[1])
	vals = data.keys()
	vals = sorted(vals, key=ftools.cmp_to_key(compare))

	for key in vals:
		expdata = data[key]

		n_neg = sum(x<0 for x in expdata)
		n_pos = sum(x>0 for x in expdata)
		print(key, " : ", n_neg, "/", n_pos)

	print(len(vals), "/", tot)
