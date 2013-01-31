#!/usr/bin/python

## Script for recognizing speech inputs for the TwitterVoice application
## Authors : Eli Pincus enp2102, Rashmi Raman rr2779




import sys
import os
ps_base = os.environ['PS_BASE']

# lab machine specific setup:
lab_machines=['gatto', 'fluffy.cs.columbia.edu', 'cheshire',
              'veu', 'dinah', 'voix', 'voce.cs.columbia.edu', 
              'chat', 'felix.cs.columbia.edu']
hostname = os.uname()[1]
if hostname in lab_machines:
    sys.path.append(ps_base + '/lib/python2.5/site-packages')
    import pocketsphinx as ps
else:
    import pocketsphinx as ps
    
# Using acoustic model 3 : HUB4 Broadcast News, 6000 senones    

am = ps_base + '/share/pocketsphinx/model/hmm/en_US/hub4_16k_6000s'

q = 'true';
# Checking for command line argument for file name, if none is given default is used. if file does not exist, exception is thrown                    
if (len(sys.argv) > 1):
    filename = sys.argv[1]
else:
    filename = 'test/enp2102_test1.wav'
try:
	f = open(filename)
except IOError:
	print "file doesn't exist"
	q='false';
if q =='true':
	# Creating an instance of the PocketSphinx decoder
	decoder = ps.Decoder(hmm=am,
			     jsgf='/proj/speech/users/cs4706/asrhw/rr2779/main_gram.jsgf',
			     dict='/proj/speech/users/cs4706/asrhw/rr2779/twitterVoice.dic')

	fh = file(filename, 'rb')
	decoder.decode_raw(fh)
	result = decoder.get_hyp()
	fh.close()

	#print
	
	# obtaining the text component of the ASR result
	result = result[0]
	print 'ASR Result:%s\n' % result

	# defining the keywords associated with each concept - twitter handle, command, confirmation, twitter action, response

	twitterHandleList = ['PAUL KRUGMAN','SUZE ORMAN','WOLF BLITZER','NICHOLAS KRISTOF','THE NEW YORK TIMES','HUFFINGTON POST']

	commandList = ['NEWS FEED','TWEETS BY','TWEETED','TRENDING TOPICS','LIST','EXIT','QUIT','HELP']
	

	#setting default values in case concept is not recognized or is not present in the utterance
	twitterHandle = 'UNSPECIFIED'
	command = 'UNSPECIFIED'
	

	#iterating through the various lists of keywords in order to recognize concepts

	if result != None:

		for i in twitterHandleList:
			if i in result:
				twitterHandle = i

		for i in commandList:
			if i in result:
				command = i

		
	#display concept table		
	print 'Twitter Handle: %s\n' % twitterHandle
	print 'Command: %s\n' % command	



		
		
