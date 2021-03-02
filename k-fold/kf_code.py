
def solution(A, K):
    p = int(len(A) / K)
    q = len(A) % K      

    unit = []
    for i in range(q):
        unit.append(p + 1)
    for i in range(K - q):
        unit.append(p)

    print(unit)

    listset = []
    pivot = 0

    for i in range(K):
        test = A[pivot : pivot + unit[i]]
        
        train1 = A[: pivot]
        train2 = A[pivot + unit[i] :]
        train = train1 + train2
        
        listset.append(train)
        listset.append(test)
        pivot += unit[i]
    
    return listset


A = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15]
K = 4

print(solution(A, K))