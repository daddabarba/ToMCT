import os
import sys
import functools as ftools

import numpy as np

from mpl_toolkits.mplot3d import axes3d
import matplotlib.pyplot as plt

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
				if(len(sys.argv)<4 or sys.argv[4]!="false"):
					print('\033[92m', "experiment ", (or0, or1, ls0, ls1) , " duplicate", '\033[0m')
			else:
				refactored[(or0, or1, ls0, ls1)] = vals
		except:
			if(len(sys.argv)<4 or sys.argv[4]!="false"):
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

	avg = {}

	for key in vals:
		expdata = data[key]

		a = np.array(expdata)
		avg[key] = np.sum(a)/len(a)

		n_neg = sum(x<0 for x in expdata)
		n_pos = sum(x>0 for x in expdata)
		# print(key, " : ", n_neg, "/", n_pos)
		if(len(sys.argv)<4 or sys.argv[4]!="false"):
			print(key, " : ", avg[key])

	if(len(sys.argv)<4 or sys.argv[4]!="false"):
		print(len(vals), "/", tot)


	o1 = int(sys.argv[2])
	o2 = int(sys.argv[3])

	fig = plt.figure()
	ax = fig.add_subplot(111, projection='3d')

	# Grab some test data.
	# X, Y, Z = axes3d.get_test_data(0.05)

	# # Plot a basic wireframe.
	# ax.plot_wireframe(X, Y, Z, rstride=10, cstride=10)

	# plt.show()

	X = np.zeros((11,11))
	Z = np.zeros((11,11))

	for i in range(0,11,1):
		for k in range(0,11,1):
			X[i][k] = i/10.0
			idx = (o1,o2,i/10.0,k/10.0) 
			if idx in avg.keys():
				Z[i][k] = avg[idx]
	Y = np.transpose(X)

	ax.plot_surface(X, Y, Z, rstride=1, cstride=1)

	plt.title(r"Delta payoff vs learning speed"
           "\n"  # Newline: the backslash is interpreted as usual
			  r"for order %d vs %d"%(o1,o2))

	ax.set_xlabel("learning speed 1")
	ax.set_ylabel("learning speed 1")
	ax.set_zlabel("delta pyoff")

	plt.savefig("data_"+str(o1)+"_"+str(o2)+".png")
