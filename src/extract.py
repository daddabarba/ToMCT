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
			refactored[(or0, or1, ls0, ls1)] = vals
		except:
			print("experiment ", file, " failed")

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

	for val in vals:
		print(val)

	print(len(vals), "/", tot)