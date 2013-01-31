#!/usr/bin/perl

use strict;

# Script for generating a sentence from given command line inputs
# Authors : Eli Pincus enp2102, Rashmi Raman rr2779

# *** update these variables with your own info ***
my $USERNAME = "rr2779";
my $TOPIC    = "twitterVoice";

# arguments  
my $sentence = shift;    # sentence to be read out

my $find = "_";
my $replace = " ";

my $wav_file = shift; # absolute path and name of the output wav file

# checks the parameters and sentence generated, printing an error message if anything is wrong.
$sentence =~ s/$find/$replace/g;

print $wav_file;
print "\n";
print $sentence;
print "\n";

if (!$wav_file || $sentence eq "") {
	die  "This script transforms an input string into an English sentence, \n"
		."synthesizes it and saves the\n"
		."results as a wav file. \n"
		
		." This script takes in parameters like twitter handle, tweet time, twitter activity, activity time ,publication,published Date \n"
		
		."Usage: tts.pl [twitterHandle], [tweetTime], [activity],[activityTime] ,[publication],[publishedDate] WAVFILE \n"
		."Where: \n"
		." twitterHandle: twitter handle  e.g. Paul Krugman , enter 0 otherwise \n"
		." tweetTime:     tweeting time e.g. 15:30, enter 0 otherwise \n"
		." activity:      activity on twitter e.g. retweet, enter 0 otherwise\n"
		." activityTime:  time that activity occurred on twitter e.g. 07:15 enter 0 otherwise\n"
		." publication:   news company that the journalist publishes his/her work in e.g. The New York Times enter 0 otherwise\n"
		." publishedDate: date the article was published in mm-dd-yyyy format e.g. 02-03-2012  enter 0 otherwise\n"		
		." WAVFILE:       absolute path and name of the output wav file.\n";
}

# full path to the TTS: partc
# (update if necessary)
my $BASEDIR = "/proj/speech/users/cs4706/".$USERNAME."/partc";


# creates a Festival script
my $filename = "/tmp/temp_".time().".scm";
open OUTPUT, ">$filename" or die "Can't open '$filename' for writing.\n";

print OUTPUT '(load "'.$BASEDIR.'/festvox/SLP_'.$TOPIC.'_xyz_ldom.scm")' . "\n";
print OUTPUT '(voice_SLP_'.$TOPIC.'_xyz_ldom)' . "\n";
print OUTPUT '(Parameter.set \'Audio_Method \'Audio_Command)' . "\n";
print OUTPUT '(Parameter.set \'Audio_Required_Rate 16000)' . "\n";
print OUTPUT '(Parameter.set \'Audio_Required_Format \'wav)' . "\n";
print OUTPUT '(Parameter.set \'Audio_Command "cp $FILE '.$wav_file.'")' . "\n";
print OUTPUT '(SayText "'. $sentence .'")' . "\n";

close OUTPUT;

# tells Festival to execute the script we just created
system "cd $BASEDIR; festival --batch $filename";


# deletes the temporary script
unlink $filename;
