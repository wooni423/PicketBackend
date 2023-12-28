#!/usr/bin/env python
# coding: utf-8

import sys
import torch
from transformers import BertTokenizer, BertModel

model_name = "bert-base-uncased"
tokenizer = BertTokenizer.from_pretrained(model_name)
model = BertModel.from_pretrained(model_name)

def text_to_embedding(text):
    inputs = tokenizer(text, return_tensors="pt", truncation=True, max_length=512)
    outputs = model(**inputs)
    return torch.mean(outputs.last_hidden_state, dim=1).detach().numpy().tolist()

if __name__ == "__main__":
    input_text = sys.argv[1] if len(sys.argv) > 1 else ""
    vector = text_to_embedding(input_text)
    print(vector)