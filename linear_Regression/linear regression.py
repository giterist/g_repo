import numpy as np
import pandas as pd
from sklearn.linear_model import LinearRegression

class AnalysisDataAndFitLinearRegression:
    
    
    def __init__(self):
        self.version = 1

    def analyse_and_fit_lrm(self, path):
        # a path to a dataset is "./data/realest.csv"
        # dataset can be loaded by uncommenting the line bellow
        # data = pd.read_csv(path)
        
        model_parameters = {}
        price_prediction = {}

        return {
            'model_parameters': model_parameters,
            'price_prediction': price_prediction
        }

    def __listwise_deletion(self, data: pd.DataFrame):
        return data.dropna()
