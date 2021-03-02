import math

# EX) input : [0.3, 0.7] or [1.0, 0.0]
# log2 아니면 log인지 확인!!
def Entropy(p):
    h_temp = 0
    for x in p:
        if x == 0: continue
        h_temp += x * (math.log(x))
    return -h_temp

print(Entropy([0.1 ,0.2 ,0.1 ]))

# ########## X와Y의 MI를 구하라
# X = [1, 2, 4, 3, 0, 0, 0] 
# Y = [1, 2, 3, 4, 0, 0, 0]
# => 모두 2.1283

X = [-3, -2, -1, 0, 1, 2, 3] 
Y = [9, 4, 1, 0, 1, 4, 9]
# H(x) == 2.8074, H(y) == 1.9502, H(x,y) == 2.8074, I(x,y) == 1.9502

def solution(X,Y):
    Px = pb_list_gen(X)
    Py = pb_list_gen(Y)

    print(Px)
    print(Py)

    Hx = Entropy(Px)
    Hy = Entropy(Py)
    
    X = list(map(str,X))
    Y = list(map(str,Y))
    XY = [x+y for x,y in zip(X,Y)]

    Pxy = pb_list_gen(XY)
    Hxy = Entropy(Pxy)

    return Hx + Hy -Hxy

def pb_list_gen(A):
    dict_a = {}
    for i in A:
        try:
            dict_a[i] += 1
        except:
            dict_a[i] = 1
    temp = []
    for i in dict_a:
        temp.append(dict_a[i] / len(A))
    
    return temp


print(solution(X,Y))




