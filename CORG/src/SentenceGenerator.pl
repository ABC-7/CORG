%Time slot prepositions
validTimePreposition(0, 'for').
getValidTimePreposition(Val):-
    random(0, 1, UID), !,
    validTimePreposition(UID, Val).

preElapsedTimePreposition(0, 'within').
preElapsedTimePreposition(1, 'during').
getPreElapsedTimePreposition(Val):-
    random(0, 1, UID), !,
    preElapsedTimePreposition(UID, Val).

%
inbetweenTimeHead(0, 'every').
inbetweenTimeHead(Val):-
    random(0, 1, UID), !,
    inbetweenTimeHead(UID, Val).

%time slot quantification
quantificationRel(0, '').
quantificationRel(1, 'at least').
quantificationRel(2, 'at most').
getQuantificationRel(Val):-
    random(0, 3, QID), !,
    quantificationRel(QID, Val).

%time slot units
unit(0, 'seconds').
unit(1, 'miliseconds').

getUnit(Val):-
    random(0, 2, UID), !,
    unit(UID, Val).


%time slot values
getValue(Val):-
    random(2, 30, Val), !.


%condition head
condHead(0,'if').
condHead(1,'provided that').
getCondHead(Val):-
    random(0, 1, UID), !,
    condHead(UID, Val).

%trigger Head
trigHead(0,'when').
getTrigHead(Val):-
    random(0, 1, UID), !,
    trigHead(UID, Val).


condScopeHead(0, 'after').
condScopeHead(2, 'while').
condScopeHead(3, 'before').
getCondScopeHead(Val):-
    random(0, 4, UID), !,
    condScopeHead(UID, Val).


subject(Subj):-
   % random(1, 3, Type),
    nounPh(Subj, 1).

nounPh(NounPh, 2):-
    count('N', NCount),
    random(1, NCount, ID1),
    random(1, NCount, ID2),
    noun(ID1, N1),
    noun(ID2, N2),
    not(N1=N2),
    concatStringlist([N1, 'of', N2], '', NounPh).

nounPh(NounPh, 1):-
    count('N', NCount),
    random(1, NCount, ID),
    noun(ID,NounPh).

verbPh(NounL, CompType, Verb, ArgL, VerbPh):-
     count(CompType, NCount),
    random(1, NCount, ID),
    verbFrame(ID, CompType, Verb, PL),
    constructVerbPh(PL, NounL, ArgL, Complement),
    concatStringlist([Verb, Complement], '', VerbPh).


constructVerbPh([], _, [], '').
% an empty item mean object
constructVerbPh([''], NounL, [Obj], Obj):-
    nounPh(Obj, 1),
    not(member(Obj, NounL)).
%atomic value
constructVerbPh(['v'|T], NounL, [Val|ArgL], VP):-
    sysValue(Val),
    not(member(Val, NounL)),!,
    constructVerbPh(T, [Val|NounL], ArgL, SubVP),
    concatStringlist([Val, SubVP], '', VP).
%atomic value
constructVerbPh(['v'|T], NounL, ArgL, VP):-
    constructVerbPh(['v'|T], NounL, ArgL, VP).
constructVerbPh([H|T], NounL, [Noun|ArgL], VP):-
     nounPh(Noun, 1),
     not(member(Noun, NounL)),!,
    constructVerbPh(T, [Noun|NounL], ArgL, SubVP),
    concatStringlist([Noun, H, SubVP], '', VP).
constructVerbPh(L, NounL, ArgL, VP):-
    constructVerbPh(L, NounL, ArgL, VP).


concatStringlist([],Str, Str).
concatStringlist([''],Str, Str).
%concatStringlist([H], _Str, H).
concatStringlist([H|T], Str, FStr):-
    H = '',
    concatStringlist(T, Str, FStr).
concatStringlist([H|T], Str, FStr):-
    not(H = ''),
    string_concat(Str, ' ', S1),
    string_concat(S1, H, NStr),
    concatStringlist(T, NStr, FStr).


% time slot
generateTimeSlot(0, Type, PrePos, [TSlot], SlotText):-
    getQuantificationRel(Quanit),
    getValue(Value),
    getUnit(Unit),
    concatStringlist([PrePos, Quanit, Value, Unit], '', SlotText),
    TSlot = slot(Type, [PrePos], [Quanit], [Value], [Unit], [], SlotText).
%no time Slot
generateTimeSlot(1, _, _PrePos, [], '').


getValidTimeSlot(Slot, Text):-
    random(0, 2, ID),
     getValidTimePreposition(Head),
    generateTimeSlot(ID, ['v-time'], Head, Slot, Text).
getPreElapsedTimeSlot(Slot, Text):-
    random(0, 2, ID),
    getPreElapsedTimePreposition(Head),
    generateTimeSlot(ID, ['pre-time'], Head, Slot, Text).
getInBetweenTimeSlot(Slot, Text):-
    random(0, 2, ID),
    inbetweenTimeHead(Head),
    generateTimeSlot(ID, ['in-time'], Head, Slot, Text).


hiddenConstrai(ParentNoun, ArgumentL,HSlot, SlotText):-
    random(0, 3, ID),
    getHiddenConstrai(ID, ParentNoun, ArgumentL,HSlot, SlotText) .
getHiddenConstrai(0, ParentNoun, [], [], ParentNoun).
getHiddenConstrai(1,ParentNoun, ArgumentL,[HSlot], SlotText):-
    property(Property),
    verbPh([Property], 'C', Verb, ArgumentL, _VerbPh),
    random(0, 10, N),
    concatStringlist([ParentNoun, 'whose', Property, Verb, N], '', SlotText),
    HSlot = slot(['hidden'], [ParentNoun], ['whose'], [Property], [Verb], [N], SlotText).
   % concatStringlist([ParentNoun, 'whose', Property, VerbPh], '', SlotText),
   % HSlot = slot(['h'], [ParentNoun], ['whose'], [Property], [Verb], ArgumentL, SlotText).

getHiddenConstrai(2, ParentNoun, ArgumentL, [HSlot], SlotText):-
    verbPh([], 'C', Verb, ArgumentL, _VerbPh),
    random(0, 10, N),
    %atom_string(N, Obj),
    concatStringlist([ParentNoun, 'that', Verb, N], '', SlotText),
    HSlot = slot(['hidden'], [ParentNoun], ['that'], [], [Verb], [N], SlotText).
   % concatStringlist([ParentNoun, 'that', VerbPh], '', SlotText),
   % HSlot = slot(['h'], [ParentNoun], ['that'], [], [Verb], ArgumentL, SlotText


condition(ExecludedNL, NExecludedNL, CompSlots, CompText, IL):-
    random(0, 2, ID),
    getCondition(ID,ExecludedNL, NExecludedNL, CompSlots, CompText, IL).
getCondition(0, ExecludedNL, ExecludedNL, [], '', [0,0,0,0]).
getCondition(1, ExecludedNL, [Subj|NArgumentL],  CompSlots, CompText, IL):-
    getCondHead(Head),
    subject(Subj),
    hiddenConstrai(Subj, HArgumentL, HSlot, HSlotText),
    append(ExecludedNL, HArgumentL, NExecludedNL),
    verbPh([Subj|NExecludedNL], 'C', Verb, ArgumentL, VerbPh),
    append(ArgumentL, NExecludedNL, NArgumentL),
    concatStringlist([Head, Subj, VerbPh], '', SlotText),
    CSlot = slot(['cond'], [], [Head], [Subj], [Verb], ArgumentL, SlotText),
    append([CSlot], HSlot, PSlots),
    getValidTimeSlot(TSlot, TSlotText),
    getPreElapsedTimeSlot(PTSlot, PTSlotText),
    L = [t(TSlot, TSlotText),t(PTSlot, PTSlotText)],
    randomReorder(L,L, NL, []),
    NL = [t(S1, T1),t(S2, T2)],
    append(PSlots, S1, Slots),
    append(Slots, S2, CompSlots),
    checkItems([[CSlot], HSlot,TSlot, PTSlot], IL),
    concatStringlist([Head, HSlotText, VerbPh, T1, T2], '', CompText).

action(ExecludedNL,  NArgumentL, CompSlots, CompText, IL):-
     random(0, 2, ID),
     getaction(ID, ExecludedNL,  NArgumentL, CompSlots, CompText, IL).
getaction(0,ExecludedNL,  NArgumentL, CompSlots, CompText, IL):-
    verbPh([], 'A', Verb, [H|ArgumentL], VerbPh),
    hiddenConstrai(H, _HArgumentL, HSlot, HSlotText),
    append([H|ArgumentL], ExecludedNL, NArgumentL),
    concatStringlist([VerbPh], '', SlotText),
    ASlot = slot(['act'], [], [], [H], [Verb], ArgumentL, SlotText),
    getValidTimeSlot(TSlot, TSlotText),
    getPreElapsedTimeSlot(PTSlot, PTSlotText),
    getInBetweenTimeSlot(RTSlot, RTSlotText),
    L = [t(TSlot, TSlotText),t(PTSlot, PTSlotText), t(RTSlot, RTSlotText)],
    randomReorder(L,L, NL, []),
    NL = [t(S1, T1),t(S2, T2), t(S3, T3)],
    append([ASlot], HSlot, PSlots),
    append(PSlots, S1, Slots),
    append(Slots, S2, NSlots),
    append(NSlots, S3, CompSlots),
    checkItems([[ASlot], HSlot, TSlot, PTSlot, RTSlot], IL),
    concatStringlist([SlotText, HSlotText,  T1, T2, T3], '', CompText).
getaction(_, ExecludedNL,  NArgumentL, CompSlots, CompText, IL):-
    verbPh([], 'A', Verb, [H|ArgumentL], VerbPh),
    hiddenConstrai(H, _HArgumentL, HSlot, HSlotText),
    append([H|ArgumentL], ExecludedNL, NArgumentL),
    concatStringlist([VerbPh], '', SlotText),
    ASlot = slot(['act'], [], [], [H], [Verb], ArgumentL, SlotText),
    getValidTimeSlot(TSlot, TSlotText),
    getPreElapsedTimeSlot(PTSlot, PTSlotText),
    getInBetweenTimeSlot(RTSlot, RTSlotText),
    L = [t(TSlot, TSlotText),t(PTSlot, PTSlotText), t(RTSlot, RTSlotText)],
    randomReorder(L,L, NL, []),
    NL = [t(S1, T1),t(S2, T2), t(S3, T3)],
    append([ASlot], HSlot, PSlots),
    append(PSlots, S1, Slots),
    append(Slots, S2, NSlots),
    append(NSlots, S3, CompSlots),
    checkItems([[ASlot], HSlot, TSlot, PTSlot, RTSlot], IL),
    concatStringlist([SlotText, HSlotText, T1, T2, T3], '', CompText).

conditionalScope(ExecludedNL, NExecludedNL, Complots, CompText, IL):-
    random(0, 2, ID),
    getConditionalScope(ID,ExecludedNL, NExecludedNL, Complots, CompText, IL).

%no conditional scope
getConditionalScope(0, ExecludedNL, ExecludedNL, [], '', [0,0,0]).
%have conditional scope
getConditionalScope(1, ExecludedNL, [Subj|NArgumentL], Slots, CompText, IL):-
    getCondScopeHead(Head),
    %nounPh(Subj,2),
    subject(Subj),
    hiddenConstrai(Subj, HArgumentL, HSlot, HSlotText),
    append(ExecludedNL, HArgumentL, _NExecludedNL),
    verbPh([Subj], 'C', Verb, ArgumentL, VerbPh),
    concatStringlist([Head, Subj, VerbPh], '', SlotText),
    CCSlot = slot(['condScope'], [], [Head], [Subj], [Verb], ArgumentL, SlotText),
    append([CCSlot], HSlot, PSlots),
    append(ArgumentL, ExecludedNL, NArgumentL),
    getValidTimeSlot(TSlot, TSlotText),
    append(PSlots, TSlot, Slots),
    checkItems([[CCSlot], HSlot, TSlot], IL),
    concatStringlist([Head, HSlotText, VerbPh, TSlotText], '', CompText).

trigger(ExecludedNL, NExecludedNL, Complots, TrigCompText, IL):-
    random(0, 2, ID),
    getTrigger(ID,ExecludedNL, NExecludedNL, Complots, TrigCompText, IL).

%no Trigger
getTrigger(0, ExecludedNL, ExecludedNL, [], '', [0,0,0,0]).
% have Trigger
getTrigger(1, ExecludedNL, [Subj|NArgumentL], CompSlots, CompText, IL):-
    getTrigHead(Head),
   % nounPh(Subj,2),
    subject(Subj),
    hiddenConstrai(Subj, HArgumentL, HSlot, HSlotText),
    append(ExecludedNL, HArgumentL, _NExecludedNL),
    verbPh([Subj], 'C', Verb, ArgumentL, VerbPh),
    concatStringlist([Head, Subj, VerbPh], '', SlotText),
    TrigSlot = slot(['trig'], [], [Head], [Subj], [Verb], ArgumentL, SlotText),
    append(ArgumentL, ExecludedNL, NArgumentL),
    append([TrigSlot], HSlot, PSlots),
    getValidTimeSlot(TSlot, TSlotText),
    getInBetweenTimeSlot(RTSlot, RTSlotText),
    L = [t(TSlot, TSlotText),t(RTSlot, RTSlotText)],
    randomReorder(L,L, NL, []),
    NL = [t(S1, T1),t(S2, T2)],
    append(PSlots, S1, Slots),
    append(Slots, S2, CompSlots),
    checkItems([[TrigSlot], HSlot, TSlot, RTSlot], IL),
    concatStringlist([Head, HSlotText, VerbPh, T1, T2], '', CompText).


loadLexicalWords:-
    consult("src/LexicalWords.pl").

internallyLoadLexicalWords:-
     consult("LexicalWords.pl").

run(Count, ReqL):-
     internallyLoadLexicalWords,
     sentence(Count, [], _SL, [], ReqL, [], _IL),
     length(ReqL, Len), writeln(Len),
     displayList(ReqL).

generateSentences(Count, ReqL):-
     loadLexicalWords,
     sentence(Count, [], _SL, [], ReqL, [], _IL).
    % length(ReqL, Len), writeln(Len).



displayList([H]):-
     writeln(H).
displayList([H|T]):-
     writeln(H), displayList(T).



sentence(0, SL,SL, ReqL, ReqL, IL, IL):-!.
sentence(Count,SL, FL, ReqL, FReqL,IL, FIL):-
    Count>0,
    conditionalScope([], _PreCCExecludedNL, PreCC_CompSlot, PreCC_CompText, PCSIL),
    trigger([], _TrigExecludedNL, Trig_CompSlot, Trig_CompText, TIL),
    condition([], _CExecludedNL, C_CompSlot, C_CompText, CIL),
    action([], _AExecludedNL, A_CompSlot, A_CompText, AIL),
    conditionalScope([], _ACCExecludedNL, ACC_CompSlot, ACC_CompText, ACSIL),

    HIL = [PCSIL, TIL, CIL, AIL, ACSIL],

    %aggregated sentence components
    TempSentComp = [comp(PreCC_CompSlot, PreCC_CompText), comp(Trig_CompSlot,Trig_CompText), comp( C_CompSlot, C_CompText), comp(A_CompSlot, A_CompText), comp(ACC_CompSlot, ACC_CompText)],

    randomReorder(TempSentComp,TempSentComp, SentComp, []),

    %aggregated sentence text
    SentComp = [comp(_C1, I1),comp(_C2, I2),comp(_C3, I3), comp(_C4, I4), comp(_C5, I5)],

   concatStringlist(['-', I1 ,',', I2, ',', I3,',', I4, ',', I5, '.'], '', SentText),

    not(member(SentText, SL)),
   % NCount is Count-1,
   delete(SentComp, comp([],''), NSentComp),

   adjustCompType(NSentComp, NNSentComp),

   updateMeasures(Count, NNSentComp, SentText, SL, ReqL, IL, HIL, NCount, NSL, NReqL, NIL),
   sentence(NCount, NSL, FL, NReqL, FReqL, NIL, FIL).


updateMeasures(Count, SentComp, SentText, SL, ReqL, IL, HIL, NCount, NSL, NReqL, NIL):-
     visableReordering(SentComp, []),!,
     % visable(NSentComp),!,
      Req = req(SentComp, SentText),
      NCount is Count-1,
      NSL = [SentText|SL],
      NReqL = [Req|ReqL],
      NIL =  [HIL|IL].
updateMeasures(Count, SentComp, _SentText, SL, ReqL, IL, _HIL, Count, SL, ReqL, IL):-
      %not(visable(NSentComp)).
     not(visableReordering(SentComp, [])).

visableReordering([H|T], L):-
    H=comp([slot([Type],_,_,_, _, _, _)|_ST],_CompT),
    not(Type = 'condScope'),
    not(member(Type, L)),
    visableReordering(T, [Type|L]).
visableReordering([],_L).


adjustCompType([H1,H2|T], Res):-
    H1 = comp([slot([Type1],E1,E2, E3, E4,E5,E6)|ST1],CompT1),
    H2 = comp([slot([Type2],_,_,_, _, _, _)|_ST],_CompT),
    Type1 = 'condScope',
    (  Type2 = 'trig' ; Type2 = 'cond' ),
    H3 = comp([slot(['precondScope'],E1,E2, E3, E4,E5,E6)|ST1],CompT1),
    adjustCompType([H2|T], RT),
    Res = [H3|RT].
adjustCompType([H1,H2|T], Res):-
    H1 = comp([slot([Type1],E1,E2, E3, E4,E5,E6)|ST1],CompT1),
    H2 = comp([slot([Type2],_,_,_, _, _, _)|_ST],_CompT),
    Type1 = 'condScope',
    Type2 = 'act',
    H3 = comp([slot(['actScope'],E1,E2, E3, E4,E5,E6)|ST1],CompT1),
    adjustCompType([H2|T], RT),
    Res = [H3|RT].
adjustCompType([H1,H2|T], Res):-
    H1 = comp([slot([Type1],_,_,_, _, _, _)|_ST1],_CompT1),
    H2 = comp([slot([Type2],E1,E2, E3, E4,E5,E6)|ST2],CompT2),
    (  Type1 = 'trig' ; Type1 = 'cond' ),
    Type2 = 'condScope',
    H3 = comp([slot(['precondScope'],E1,E2, E3, E4,E5,E6)|ST2],CompT2),
    adjustCompType(T, RT),
    Res = [H1, H3|RT].
adjustCompType([H1,H2|T], Res):-
    H1 = comp([slot([Type1],_,_,_, _, _, _)|_ST1],_CompT1),
    H2 = comp([slot([Type2],E1,E2, E3, E4,E5,E6)|ST2],CompT2),
    Type1 = 'act',
    Type2 = 'condScope',
    H3 = comp([slot(['actScope'],E1,E2, E3, E4,E5,E6)|ST2],CompT2),
    adjustCompType(T, RT),
    Res = [H1, H3|RT].
adjustCompType([H|T], [H|RT]):-
    adjustCompType(T, RT).
adjustCompType([], []).


replaceAt(Element,0,[_H|L],AccL, FL):-
    append(AccL, [Element], NL),
    append(NL, L, FL).
replaceAt(Element,Pos,[E|L],ACCL, FL):-
    Pos1 is Pos-1,
    append(ACCL, [E], NACCL),
    replaceAt(Element,Pos1,L, NACCL,FL).

insertRandomly(Element,L,NL, Idcies, NIdcies):-
    length(L, Len),
    random(0,Len, Idx),
    not(member(Idx, Idcies)),!,
    NIdcies = [Idx|Idcies],
    replaceAt(Element,Idx, L, [], NL).
insertRandomly(Element,L,NL, Idcies, NIdcies):-
    insertRandomly(Element,L,NL, Idcies, NIdcies).

randomReorder([], L, L,_I):-!.
randomReorder([H|T], L, Res, Indicies):-
    insertRandomly(H,L,NL, Indicies, NIndicies),
    randomReorder(T, NL, Res, NIndicies).
checkItems([],[]).
checkItems([[]|T1], [0|T2]):-
    checkItems(T1, T2).
checkItems([_H|T1], [1|T2]):-
    checkItems(T1, T2).
