count('N', 14).
count('A', 4).
count('C', 3).
noun(1,'the regulator mode').
noun(2,'the output regulator status').
noun(3,'the signal').
noun(4,'the heat control').
noun(5,'the manage monitor interface mode').
noun(6,'the output monitor status').
noun(7,'the monitor mode').
noun(8,'the alarm control').
noun(9,'the regulator status').
noun(10,'the monitor status').
noun(11,'the monitor init timeout').
noun(12,'the regulator init timeout').
noun(13,'the Reset').
verbFrame(1,'A','set',['to','v']).
verbFrame(2,'A','set',['to','']).
verbFrame(3,'A','initialize',['to','v']).
verbFrame(1,'C','equal',['v']).
verbFrame(2,'C','is',['v']).
sysValue('INIT').
sysValue('NORMAL').
sysValue('Status_On').
sysValue('FAILED').
sysValue('ok').
sysValue('Control_Off').
sysValue('True').
sysValue('False').
property('mode').
